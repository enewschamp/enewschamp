package com.enewschamp.security.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.security.service.AppSecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppSecFilter extends GenericFilterBean {

	@Autowired
	AppSecurityService appSecService;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		System.out.println("Entered Filter");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String appName = request.getHeader("appName");
		String appKey = request.getHeader("appKey");
		PrintWriter writer = response.getWriter();
		String payLoad = this.getRequestBody(request);
		PageRequestDTO requestPage = objectMapper.readValue(payLoad, PageRequestDTO.class);
		System.out.println(requestPage.getHeader().getAction());

		if ("".equals(appName) || null == appName) {
			HeaderDTO header = requestPage.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			writer.write(objectMapper.writeValueAsString(header));
			writer.flush();
			return;
		}
		if ("".equals(appKey) || null == appKey) {
			HeaderDTO header = requestPage.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			writer.write(objectMapper.writeValueAsString(header));
			writer.flush();
			return;
		}
		boolean isValid = appSecService.isValidKey(appName, appKey);
		if (!isValid) {
			HeaderDTO header = requestPage.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			writer.write(objectMapper.writeValueAsString(header));
			writer.flush();
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String getRequestBody(final HttpServletRequest request) {

		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = requestWrapper.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		} catch (IOException ex) {
			// log.error("Error reading the request payload", ex);
			// throw new AuthenticationException("Error reading the request payload", ex);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iox) {
					// ignore
				}
			}
		}
		// System.out.print(stringBuilder.toString());
		return stringBuilder.toString();
	}

}
