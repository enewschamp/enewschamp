package com.enewschamp.app.admin.student.registration.bulk.handler;

import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("BulkStudentRegistrationPageHandler")
@RequiredArgsConstructor
@Slf4j
public class BulkStudentRegistrationPageHandler implements IPageHandler {
	private final BulkStudentRegistrationService bulkStudentRegistrationServie;

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			try {
				pageDto = bulkStudentRegistrationServie.insertBulkStudentRegistration(pageRequest);
			} catch (Exception e) {
				if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
				}
//				if(e.getCause().getCause() instanceof javax.validation.ConstraintViolationException) {
//					throw new BusinessException(ErrorCodeConstants.RECORD_DATA_ISSUE);
//				}
			}
			break;
		case "List":
			pageDto = bulkStudentRegistrationServie.findAll(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

}
