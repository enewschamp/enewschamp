package com.enewschamp.app.admin.user.login.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("UserLoginPageHandler")
public class UserLoginPageHandler implements IPageHandler {
	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Update":
			pageDto = updateUserLogin(pageRequest);
			break;
		case "Read":
			pageDto = readUserLogin(pageRequest);
			break;
		case "Close":
			pageDto = closeUserLogin(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateUserLogin(pageRequest);
			break;
		case "List":
			pageDto = listUserLogin(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

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

	@SneakyThrows
	private PageDTO createUserLogin(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLoginPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLoginPageData.class);
		validate(pageData,  this.getClass().getName());
		UserLogin userLogin = mapUserLoginData(pageRequest, pageData);
		userLogin = userLoginService.create(userLogin);
		mapUserLogin(pageRequest, pageDto, userLogin);
		return pageDto;
	}


	private UserLogin mapUserLoginData(PageRequestDTO pageRequest, UserLoginPageData pageData) {
		UserLogin userLogin = modelMapper.map(pageData, UserLogin.class);
		userLogin.setRecordInUse(RecordInUseType.Y);
		return userLogin;
	}

	@SneakyThrows
	private PageDTO updateUserLogin(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLoginPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLoginPageData.class);
		validate(pageData,  this.getClass().getName());
		UserLogin userLogin = mapUserLoginData(pageRequest, pageData);
		userLogin = userLoginService.update(userLogin);
		mapUserLogin(pageRequest, pageDto, userLogin);
		return pageDto;
	}

	private void mapUserLogin(PageRequestDTO pageRequest, PageDTO pageDto, UserLogin userLogin) {
		UserLoginPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(userLogin);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readUserLogin(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLoginPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLoginPageData.class);
		UserLogin userLogin = modelMapper.map(pageData, UserLogin.class);
		userLogin = userLoginService.read(userLogin);
		mapUserLogin(pageRequest, pageDto, userLogin);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateUserLogin(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLoginPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLoginPageData.class);
		UserLogin userLogin = modelMapper.map(pageData, UserLogin.class);
		userLogin = userLoginService.reInstate(userLogin);
		mapUserLogin(pageRequest, pageDto, userLogin);
		return pageDto;
	}

	private UserLoginPageData mapPageData(UserLogin userLogin) {
		UserLoginPageData pageData = modelMapper.map(userLogin, UserLoginPageData.class);
		pageData.setLastUpdate(userLogin.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeUserLogin(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLoginPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLoginPageData.class);
		UserLogin userLogin = modelMapper.map(pageData, UserLogin.class);
		userLogin = userLoginService.close(userLogin);
		mapUserLogin(pageRequest, pageDto, userLogin);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUserLogin(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UserLogin> userLoginList = userLoginService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UserLoginPageData> list = mapUserLoginData(userLoginList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((userLoginList.getNumber() + 1) == userLoginList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<UserLoginPageData> mapUserLoginData(Page<UserLogin> page) {
		List<UserLoginPageData> userLoginPageDataList = new ArrayList<UserLoginPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UserLogin> pageDataList = page.getContent();
			for (UserLogin userLogin : pageDataList) {
				UserLoginPageData userLoginPageData = modelMapper.map(userLogin, UserLoginPageData.class);
				userLoginPageData.setLastUpdate(userLogin.getOperationDateTime());
				userLoginPageDataList.add(userLoginPageData);
			}
		}
		return userLoginPageDataList;
	}


}