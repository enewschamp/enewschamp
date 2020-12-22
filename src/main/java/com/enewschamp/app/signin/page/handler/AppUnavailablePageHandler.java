package com.enewschamp.app.signin.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "AppUnavailablePageHandler")
public class AppUnavailablePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	UserLoginBusiness userLoginBusiess;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		AppUnavailablePageData unavailablePageData = new AppUnavailablePageData();
		unavailablePageData.setMessage("App is temporarily unavailable!! Please try again after sometime!!");
		pageDto.setData(unavailablePageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {

		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO handleRefreshAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}
}
