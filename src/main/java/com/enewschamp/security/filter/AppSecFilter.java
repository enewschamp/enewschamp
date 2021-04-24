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

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.security.service.AppSecurityService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppSecFilter extends GenericFilterBean {

	@Autowired
	AppSecurityService appSecService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String callbackURL = propertiesService.getValue("StudentApp", PropertyConstants.PAYTM_CALLBACK_URL);
		final MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest(request);
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (request.getRequestURL().toString().equalsIgnoreCase(callbackURL)) {
			filterChain.doFilter(request, response);
			return;
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "180");
		String appName = requestWrapper.getHeader("appName");
		String appKey = requestWrapper.getHeader("appKey");
		String module = requestWrapper.getHeader("module");
		String payLoad = this.getRequestBody(requestWrapper);
		PageRequestDTO requestPage = null;
		if (payLoad != null && !payLoad.isEmpty()) {
			try {
				requestPage = objectMapper.readValue(payLoad, PageRequestDTO.class);
			} catch (Exception e) {
				response.setContentType("application/json");
				PrintWriter writer = response.getWriter();
				HeaderDTO header = new HeaderDTO();
				header.setRequestStatus(RequestStatusType.F);
				BusinessException be = new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
				Fault f = new Fault(be, header);
				f.getData().setErrorMessageParams(null);
				writer.write(objectMapper.writeValueAsString(f));
				writer.flush();
				return;
			}
		}
		HeaderDTO header = new HeaderDTO();
		if (requestPage != null) {
			header = requestPage.getHeader();
		}
		if ("".equals(appName) || null == appName) {
			appName = servletRequest.getParameter("appName");
		}
		if ("".equals(appKey) || null == appKey) {
			appKey = servletRequest.getParameter("appKey");
		}
		if ("".equals(module) || null == module) {
			module = servletRequest.getParameter("module");
		}
		// validate the appkey and appname
		if ("".equals(appName) || null == appName) {
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			header.setRequestStatus(RequestStatusType.F);
			BusinessException be = new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
			Fault f = new Fault(be, header);
			f.getData().setErrorMessageParams(null);
			requestPage = modelMapper.map(f, PageRequestDTO.class);
			String json = objectMapper.writeValueAsString(f.getData());
			JsonNode jsonNode = objectMapper.readTree(json);
			requestPage.setData(jsonNode);
			writer.write(objectMapper.writeValueAsString(requestPage));
			writer.flush();
			return;
		}
		if ("".equals(appKey) || null == appKey) {
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			header.setRequestStatus(RequestStatusType.F);
			BusinessException be = new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
			Fault f = new Fault(be, header);
			f.getData().setErrorMessageParams(null);
			requestPage = modelMapper.map(f, PageRequestDTO.class);
			String json = objectMapper.writeValueAsString(f.getData());
			JsonNode jsonNode = objectMapper.readTree(json);
			requestPage.setData(jsonNode);
			writer.write(objectMapper.writeValueAsString(requestPage));
			writer.flush();
			return;
		}
		if (header != null && header.getModule() != null) {
			module = header.getModule();
		}
		boolean isValid = appSecService.isValidKey(appName, appKey, module);
		if (!isValid) {
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			if (header != null) {
				header.setRequestStatus(RequestStatusType.F);
			}
			BusinessException be = new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
			Fault f = new Fault(be, header);
			f.getData().setErrorMessageParams(null);
			requestPage = modelMapper.map(f, PageRequestDTO.class);
			String json = objectMapper.writeValueAsString(f.getData());
			JsonNode jsonNode = objectMapper.readTree(json);
			requestPage.setData(jsonNode);
			writer.write(objectMapper.writeValueAsString(requestPage));
			writer.flush();
			return;
		}
		filterChain.doFilter(requestWrapper, servletResponse);
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
			System.out.println("Error reading the request payload : " + ex);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iox) {
				}
			}
		}
		return stringBuilder.toString();
	}

}
