package com.enewschamp.app.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.CommonModuleService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class AdminPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private CommonModuleService commonModuleService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@PostMapping(value = "/admin")
	public ResponseEntity<PageDTO> processAdminRequest(@RequestBody PageRequestDTO pageRequest) {
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
			commonModuleService.validateHeaders(pageRequest.getHeader(), module);
			// commonModuleService.doEntitlementCheck(pageRequest.getHeader());
			UserActivityTracker userActivityTracker = commonModuleService.validateUser(pageRequest, module, pageName,
					actionName, loginCredentials, userId, deviceId, operation, editionId, UserType.A,
					PropertyConstants.ADMIN_MODULE_NAME);
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);
			PageDTO pageResponse = null;
			if (actionName.equalsIgnoreCase("RefreshToken")) {
				pageResponse = commonModuleService.performRefreshToken(pageRequest, loginCredentials, userId, deviceId,
						userActivityTracker, UserType.A);
			} else {
				pageResponse = processRequest(pageName, actionName, pageRequest, "admin");
				pageResponse.getHeader().setLoginCredentials(null);
			}
			if (pageName.equals("Login"))
				pageResponse.setData(
						commonModuleService.getLoginPageData(module, userId, UserType.A, deviceId, loginCredentials));
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setTodaysDate(LocalDate.now());
			header.setRequestStatus(RequestStatusType.F);
			throw new Fault(e, header);
		}
		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		// Process current page
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAction(pageRequest);
		return pageResponse;
	}

	@RequestMapping(value = "/admin/images", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public void getImage(HttpServletResponse response, @RequestParam String imageType, @RequestParam String imagePath,
			@RequestParam String appKey, @RequestParam String appName, @RequestParam String module,
			@RequestParam String userId, @RequestParam String deviceId, @RequestParam String loginCredentials)
			throws IOException {
		if (imageType == null || imagePath == null || appKey == null || appName == null || deviceId == null
				|| loginCredentials == null || imageType.trim().isEmpty() || imagePath.trim().isEmpty()
				|| appKey.trim().isEmpty() || appName.trim().isEmpty() || deviceId.trim().isEmpty()
				|| loginCredentials.trim().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
		} else {
			boolean loginCheckFlag = false;
			String validateLogin = propertiesService.getValue(module, PropertyConstants.VALIDATE_LOGIN);
			String loginRequired[] = validateLogin.split("\\|");
			for (int i = 0; i < loginRequired.length; i++) {
				if (imageType.equalsIgnoreCase(loginRequired[i])) {
					loginCheckFlag = true;
					break;
				}
			}
			try {
				if (loginCheckFlag) {
					if (userId == null || userId.trim().isEmpty()) {
						throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
					}
					;
					User user = userService.get(userId);
					if (user == null) {
						throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
					} else {
						userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, UserType.P);
					}
				} else {
					UserLogin deviceLogin = userLoginBusiness.getBODeviceLogin(deviceId, loginCredentials);
					if (deviceLogin == null) {
						throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
					}
				}
				if (imagePath.startsWith("/")) {
					imagePath = imagePath.substring(2, imagePath.length());
				}
				String imageFolderPath = propertiesService.getValue(module, "imageRootFolderPath." + imageType);
				File imageFile = new File(imageFolderPath + imagePath);
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
					imageStream.close();
				}
			} catch (BusinessException e) {
				throw new Fault(e);
			}
		}
	}
}
