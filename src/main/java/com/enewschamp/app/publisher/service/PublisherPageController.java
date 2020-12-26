package com.enewschamp.app.publisher.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.CommonModuleService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.HashTagService;
import com.enewschamp.user.domain.service.UserService;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/enewschamp-api/v1")
public class PublisherPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@Autowired
	EditionService editionService;

	@Autowired
	HashTagService hashTagService;
	
	@Autowired
	private CommonModuleService commonModuleService;

	@PostMapping(value = "/publisher")
	@Transactional
	public ResponseEntity<PageDTO> processPublisherAppRequest(@RequestBody PageRequestDTO pageRequest) {
		ResponseEntity<PageDTO> response = null;
		try {
			String module = pageRequest.getHeader().getModule();
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			String loginCredentials = pageRequest.getHeader().getLoginCredentials();
			String userId = pageRequest.getHeader().getUserId();
			String deviceId = pageRequest.getHeader().getDeviceId();
			String operation = pageRequest.getHeader().getOperation();
			String editionId = pageRequest.getHeader().getEditionId();
			if (module == null || pageName == null || operation == null || actionName == null || userId == null
					|| deviceId == null || loginCredentials == null || editionId == null
					|| (!propertiesService.getProperty(PropertyConstants.PUBLISHER_MODULE_NAME).equals(module))
					|| pageName.trim().isEmpty() || actionName.trim().isEmpty() || deviceId.trim().isEmpty()
					|| editionId.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			}
			UserActivityTracker userActivityTracker = commonModuleService.validateUser(pageRequest, module, pageName, actionName,
					loginCredentials, userId, deviceId, operation, editionId, UserType.P, PropertyConstants.PUBLISHER_MODULE_NAME);
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);

			PageDTO pageResponse = null;
			if (actionName.equalsIgnoreCase("RefreshToken")) {
				pageResponse = commonModuleService.performRefreshToken(pageRequest, loginCredentials, userId, deviceId,
						userActivityTracker, UserType.P);
			} else {
				pageResponse = processRequest(pageName, actionName, pageRequest, "publisher");
				pageResponse.getHeader().setLoginCredentials(null);
			}
			pageResponse.getHeader().setDeviceId(null);
			pageResponse.getHeader().setUserId(null);
			pageResponse.getHeader().setEditionId(null);
			pageResponse.getHeader().setTodaysDate(LocalDate.now());
			String helpText = propertiesService
					.getProperty("publisher." + pageResponse.getHeader().getPageName().toLowerCase());
			pageResponse.getHeader().setHelpText(helpText);
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			header.setUserId(null);
			header.setEditionId(null);
			header.setLoginCredentials(null);
			header.setDeviceId(null);
			header.setTodaysDate(LocalDate.now());
			throw new Fault(e, header);
		} catch (Exception e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			header.setUserId(null);
			header.setEditionId(null);
			header.setLoginCredentials(null);
			header.setDeviceId(null);
			header.setTodaysDate(LocalDate.now());
			throw new Fault(e.getLocalizedMessage(), header);
		}
		return response;
	}



	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		// Process current page
		String edition = pageRequest.getHeader().getEditionId();
		// check if the edition exist..
		editionService.getEdition(edition);
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAction(pageRequest);

		String nextPageName = null;
		// Load next page
		Map<String, String> fromPageWiseActions = appConfig.getPageNavigationConfig().get(context)
				.get(pageName.toLowerCase());
		if (fromPageWiseActions != null) {
			nextPageName = fromPageWiseActions.get(actionName.toLowerCase());
		}
		System.out.println(">>>>>>fromPageWiseActions>>>>>>>>" + fromPageWiseActions);
		System.out.println(">>>>>>nextPageName>>>>>>>>" + nextPageName);
		System.out.println(">>>>>>actionName>>>>>>>>" + actionName);
		System.out.println(">>>>>>pageName>>>>>>>>" + pageName);
		if (nextPageName == null) {
			throw new BusinessException(ErrorCodeConstants.NEXT_PAGE_NOT_FOUND, pageName, actionName);
		}
		if (!pageName.equals(nextPageName) && !nextPageName.isEmpty()) {
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName, context).loadPage(pageNavigationContext);
		}
		addSuccessHeader(pageName, actionName, pageResponse, context);
		return pageResponse;
	}

	private void addSuccessHeader(String currentPageName, String actionName, PageDTO page, String context) {
		if (page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(page.getPageName());

		String nextPageName = appConfig.getPageNavigationConfig().get(context).get(currentPageName.toLowerCase())
				.get(actionName.toLowerCase());
		page.getHeader().setPageName(nextPageName);
	}

	@RequestMapping(value = "/publisher/images/article", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void getArticleImage(HttpServletResponse response, @RequestParam String imagePath,
			@RequestParam String appKey, @RequestParam String appName, @RequestParam String userId,
			@RequestParam String deviceId, @RequestParam String loginCredentials) throws IOException {
		try {
			if (imagePath == null || appKey == null || appName == null || userId == null || deviceId == null
					|| loginCredentials == null) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);

			} else if (imagePath.trim().isEmpty() || appKey.trim().isEmpty() || appName.trim().isEmpty()
					|| userId.trim().isEmpty() || deviceId.trim().isEmpty() || loginCredentials.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			}
			if (!userService.validateUser(userId)) {
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
			} else {
				userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, UserType.P);
			}
			if (imagePath.startsWith("/")) {
				imagePath = imagePath.substring(2, imagePath.length());
			}
			String imagesFolderPath = propertiesService.getProperty(PropertyConstants.ARTICLE_IMAGE_CONFIG_FOLDER_PATH);
			File imageFile = new File(imagesFolderPath + imagePath);
			if (imageFile != null && imageFile.exists()) {
				InputStream imageStream = new FileInputStream(imageFile);
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				StreamUtils.copy(imageStream, response.getOutputStream());
			}
		} catch (BusinessException e) {
			throw new Fault(e);
		}
	}

	@RequestMapping(value = "/publisher/images/user", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void getUserImage(HttpServletResponse response, @RequestParam String imagePath, @RequestParam String appKey,
			@RequestParam String appName, @RequestParam String userId, @RequestParam String deviceId,
			@RequestParam String loginCredentials) throws IOException {
		try {
			if (imagePath == null || appKey == null || appName == null || userId == null || deviceId == null
					|| loginCredentials == null) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);

			} else if (imagePath.trim().isEmpty() || appKey.trim().isEmpty() || appName.trim().isEmpty()
					|| userId.trim().isEmpty() || deviceId.trim().isEmpty() || loginCredentials.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			}
			if (!userService.validateUser(userId)) {
				throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
			} else {
				userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, UserType.P);
			}
			if (imagePath.startsWith("/")) {
				imagePath = imagePath.substring(2, imagePath.length());
			}
			String imagesFolderPath = propertiesService.getProperty(PropertyConstants.USER_IMAGE_CONFIG_FOLDER_PATH);
			File imageFile = new File(imagesFolderPath + imagePath);
			if (imageFile != null && imageFile.exists()) {
				InputStream imageStream = new FileInputStream(imageFile);
				if (imagePath.toUpperCase().endsWith(".JPG") || imagePath.toUpperCase().endsWith(".JEPG")) {
					response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				} else if (imagePath.toUpperCase().endsWith(".PNG")) {
					response.setContentType(MediaType.IMAGE_PNG_VALUE);
				} else if (imagePath.toUpperCase().endsWith(".GIF")) {
					response.setContentType(MediaType.IMAGE_GIF_VALUE);
				}
				StreamUtils.copy(imageStream, response.getOutputStream());
			}
		} catch (BusinessException e) {
			throw new Fault(e);
		}
	}

}
