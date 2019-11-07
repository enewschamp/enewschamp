package com.enewschamp.app.publisher.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
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
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.java.Log;

@Log
@RestController
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
			//SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("deepak", "welcome"));
			
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			String userId = pageRequest.getHeader().getUserId();
			String deviceId = pageRequest.getHeader().getDeviceId();
			
			// Check if user has been logged in
			if (!(pageName.equalsIgnoreCase("Login")
					&& (actionName.equalsIgnoreCase("login") 
							|| actionName.equalsIgnoreCase("logout")
							|| actionName.equalsIgnoreCase("ResetPassword")))) {
				
				if (null == userId || "".equals(userId) || !userService.validateUser(userId)) {
					throw new BusinessException(ErrorCodes.INVALID_USER_ID, userId);
				} else {
					boolean isLogged = userLoginBusiness.isUserLoggedIn(deviceId, userId, UserType.A);
					if (!isLogged) {
						throw new BusinessException(ErrorCodes.UNAUTH_ACCESS, "UnAuthorised Access");
					}
				}
				JsonNode dataNode = pageRequest.getData();
				if(dataNode != null && dataNode instanceof ObjectNode) {
					((ObjectNode)dataNode).put("operatorId", userId);
				}
			}
			
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);
			
			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "publisher");
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch(BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if(header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), e, header);
		}
		 
		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		//Process current page
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAction(actionName, pageRequest);
		
		String nextPageName = null;
		//Load next page
		Map<String, String> fromPageWiseActions = appConfig.getPageNavigationConfig().get(context).get(pageName.toLowerCase());
		if(fromPageWiseActions != null) {
			nextPageName = fromPageWiseActions.get(actionName.toLowerCase());
		}
		if(nextPageName == null) {
			throw new BusinessException(ErrorCodes.NEXT_PAGE_NOT_FOUND, pageName, actionName);
		}
		if(!pageName.equals(nextPageName) && !nextPageName.isEmpty()) {
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
		if(page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(page.getPageName());
		
		String nextPageName = appConfig.getPageNavigationConfig().get(context).get(currentPageName.toLowerCase()).get(actionName.toLowerCase());
		page.getHeader().setPageName(nextPageName);
	}
	
	@RequestMapping(value = "/images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response, @RequestParam String imagePath) throws IOException {
		if(imagePath.startsWith("/")) {
			imagePath = imagePath.substring(2, imagePath.length());
		}
		String imagesFolderPath =  appConfig.getArticleImageConfig().getImagesRootFolderPath();
        File imageFile = new File(imagesFolderPath + imagePath);
        InputStream imageStream = new FileInputStream(imageFile);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imageStream, response.getOutputStream());
    }
	
}
