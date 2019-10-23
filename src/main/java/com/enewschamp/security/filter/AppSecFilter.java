package com.enewschamp.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.service.AppSecurityService;

@Configuration
public class AppSecFilter extends GenericFilterBean{

	@Autowired
	AppSecurityService appSecService;
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		System.out.println("Entered Filter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String appName = request.getHeader("appName");
		String appKey = request.getHeader("appKey");

		if ("".equals(appName) || null == appName) {
			throw new BusinessException(ErrorCodes.APP_SEC_KEY_NOT_FOUND, "Unsecured access");

			//response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			//return;
		}
		if ("".equals(appKey) || null == appKey) {
			throw new BusinessException(ErrorCodes.APP_SEC_KEY_NOT_FOUND, "Unsecured access");

		//	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			//return;
		}
		boolean isValid = appSecService.isValidKey(appName, appKey);
		if(!isValid)
		{
			throw new BusinessException(ErrorCodes.APP_SEC_KEY_NOT_FOUND, "Unsecured access");
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

}
