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
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/enewschamp-api/v1")
public class PublisherPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@PostMapping(value = "/publisher")
	@Transactional
	public ResponseEntity<PageDTO> processPublisherAppRequest(@RequestBody PageRequestDTO pageRequest) {

		ResponseEntity<PageDTO> response = null;
		try {
			// SecurityContextHolder.getContext().setAuthentication(new
			// UsernamePasswordAuthenticationToken("deepak", "welcome"));

<<<<<<< Updated upstream
=======
			String module = pageRequest.getHeader().getModule();
>>>>>>> Stashed changes
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			String loginCredentials = pageRequest.getHeader().getLoginCredentials();
			String userId = pageRequest.getHeader().getUserId();
			String deviceId = pageRequest.getHeader().getDeviceId();
<<<<<<< Updated upstream

			// Check if user has been logged in
<<<<<<< Updated upstream
			if (!(pageName.equalsIgnoreCase("Login")
					&& (actionName.equalsIgnoreCase("login") 
							|| actionName.equalsIgnoreCase("logout")
							|| actionName.equalsIgnoreCase("ResetPassword")))) {
				
=======
			if (!(pageName.equalsIgnoreCase("Login") && (actionName.equalsIgnoreCase("login")
					|| actionName.equalsIgnoreCase("logout") || actionName.equalsIgnoreCase("ResetPassword")))) {

>>>>>>> Stashed changes
=======
			String operation = pageRequest.getHeader().getOperation();

			if (module == null || pageName == null || actionName == null || userId == null || deviceId == null
					|| operation == null || loginCredentials == null
					|| (!appConfig.getPublisherModuleName().equals(module)) || pageName.trim().isEmpty()
					|| actionName.trim().isEmpty() || deviceId.trim().isEmpty()) {
				throw new BusinessException(ErrorCodes.INVALID_REQUEST,
						"Invalid Request. Missing mandatory request parameters.");

			}

			pageRequest.getHeader().setPageSize(appConfig.getPageSize());

			// Check if user has been logged in
			if (!(pageName.equalsIgnoreCase("Login") && (actionName.equalsIgnoreCase("login")
					|| actionName.equalsIgnoreCase("logout") || actionName.equalsIgnoreCase("ResetPassword")))) {

>>>>>>> Stashed changes
				if (null == userId || "".equals(userId) || !userService.validateUser(userId)) {
					throw new BusinessException(ErrorCodes.INVALID_USER_ID, userId);
				} else {
					boolean isLogged = userLoginBusiness.isUserLoggedIn(deviceId, userId, UserType.A);
					if (!isLogged) {
						throw new BusinessException(ErrorCodes.UNAUTH_ACCESS, "UnAuthorised Access");
					}
				}
				JsonNode dataNode = pageRequest.getData();
				if (dataNode != null && dataNode instanceof ObjectNode) {
					((ObjectNode) dataNode).put("operatorId", userId);
				}
			}

			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);
<<<<<<< Updated upstream
			pageRequest.getHeader().setUserRole(userLoginBusiness.getUserRole(userId));
			User user = userService.get(userId);
			pageRequest.getHeader().setUserName(user.getName() + " " + user.getSurname());
			pageRequest.getHeader().setUserPic(user.getPhoto() == null ? "" : user.getPhoto());
			pageRequest.getHeader().setTodaysDate(LocalDate.now());

=======
			User user = userService.get(userId);
			if (actionName.equalsIgnoreCase("login")) {
				pageRequest.getHeader().setUserName(user.getName() + " " + user.getSurname());
				pageRequest.getHeader().setUserPic(user.getPhoto() == null ? "" : user.getPhoto());
				pageRequest.getHeader().setTodaysDate(LocalDate.now());
				pageRequest.getHeader().setUserRole(userLoginBusiness.getUserRole(userId));
			}
>>>>>>> Stashed changes
			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "publisher");
			pageResponse.getHeader().setLoginCredentials(null);
			pageResponse.getHeader().setPageSize(null);
			pageResponse.getHeader().setDeviceId(null);
			if (!actionName.equalsIgnoreCase("login")) {
				pageResponse.getHeader().setUserId(null);
				pageResponse.getHeader().setUserName(null);
				pageResponse.getHeader().setUserPic(null);
				pageResponse.getHeader().setTodaysDate(null);
				pageResponse.getHeader().setUserRole(null);
				pageResponse.getHeader().setEditionId(null);
			}
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			header.setUserId(null);
			header.setUserName(null);
			header.setUserPic(null);
			header.setTodaysDate(null);
			header.setUserRole(null);
			header.setEditionId(null);
			header.setLoginCredentials(null);
			header.setPageSize(null);
			header.setDeviceId(null);

			// throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), e,
			// header);
			throw new Fault(new HttpStatusAdapter(HttpStatus.OK), e, header);
		}

		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		// Process current page
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAction(actionName,
				pageRequest);

		String nextPageName = null;
		// Load next page
		Map<String, String> fromPageWiseActions = appConfig.getPageNavigationConfig().get(context)
				.get(pageName.toLowerCase());
<<<<<<< Updated upstream
		if (fromPageWiseActions != null) {
			nextPageName = fromPageWiseActions.get(actionName.toLowerCase());
		}
=======
		System.out.println(">>>>>>fromPageWiseActions>>>>>>>>" + fromPageWiseActions);
		if (fromPageWiseActions != null) {
			nextPageName = fromPageWiseActions.get(actionName.toLowerCase());
		}
		System.out.println(">>>>>>nextPageName>>>>>>>>" + nextPageName);
		System.out.println(">>>>>>actionName>>>>>>>>" + actionName);
		System.out.println(">>>>>>pageName>>>>>>>>" + pageName);
>>>>>>> Stashed changes
		if (nextPageName == null) {
			throw new BusinessException(ErrorCodes.NEXT_PAGE_NOT_FOUND, pageName, actionName);
		}
		if (!pageName.equals(nextPageName) && !nextPageName.isEmpty()) {
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageNavigationContext.setActionName(actionName);
			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			System.out.println(">>>>>>pageResponse1>>>>>>>>" + pageResponse);
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName, context).loadPage(pageNavigationContext);
			System.out.println(">>>>>>pageResponse2>>>>>>>>" + pageResponse);
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

	@RequestMapping(value = "/images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImage(HttpServletResponse response, @RequestParam String imagePath) throws IOException {
		if (imagePath.startsWith("/")) {
			imagePath = imagePath.substring(2, imagePath.length());
		}
		String imagesFolderPath = appConfig.getArticleImageConfig().getImagesRootFolderPath();
		File imageFile = new File(imagesFolderPath + imagePath);
		InputStream imageStream = new FileInputStream(imageFile);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(imageStream, response.getOutputStream());
	}

}
