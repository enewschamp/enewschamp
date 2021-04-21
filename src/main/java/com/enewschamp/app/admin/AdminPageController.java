package com.enewschamp.app.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.CommonModuleService;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class AdminPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private CommonModuleService commonModuleService;

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

}
