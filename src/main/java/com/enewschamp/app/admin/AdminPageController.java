package com.enewschamp.app.admin;

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
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class AdminPageController {
	
	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	@PostMapping(value="/admin")
	public ResponseEntity<PageDTO> processAdminRequest(@RequestBody PageRequestDTO pageRequest){
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
