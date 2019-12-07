package com.enewschamp.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	ModelMapper modelMapper;

	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {

		ResponseEntity<PageDTO> response = null;
		try {
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);

			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "app");
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
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

		// Load next page
		String nextPageName = appConfig.getPageNavigationConfig().get(context).get(pageName.toLowerCase())
				.get(actionName.toLowerCase());
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

}
