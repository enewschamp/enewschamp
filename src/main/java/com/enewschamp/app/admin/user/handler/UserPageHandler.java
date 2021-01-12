package com.enewschamp.app.admin.user.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UserPageHandler")
@Slf4j
public class UserPageHandler implements IPageHandler {
	@Autowired
	private UserService userService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createUser(pageRequest);
			break;
		case "Update":
			pageDto = updateUser(pageRequest);
			break;
		case "Read":
			pageDto = readUser(pageRequest);
			break;
		case "Close":
			pageDto = closeUser(pageRequest);
			break;
		case "Reinuser":
			pageDto = reInstateUser(pageRequest);
			break;
		case "List":
			pageDto = listUser(pageRequest);
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
	private PageDTO createUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		validateData(pageData);
		User user = mapUserData(pageRequest, pageData);
		user = userService.create(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);

	}

	private User mapUserData(PageRequestDTO pageRequest, UserPageData pageData) {
		User user = modelMapper.map(pageData, User.class);
		user.setRecordInUse(RecordInUseType.Y);
		return user;
	}

	@SneakyThrows
	private PageDTO updateUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		validateData(pageData);
		User user = mapUserData(pageRequest, pageData);
		user = userService.update(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	private void mapUser(PageRequestDTO pageRequest, PageDTO pageDto, User user) {
		UserPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(user);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.read(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.reInstate(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	private UserPageData mapPageData(User user) {
		UserPageData pageData = modelMapper.map(user, UserPageData.class);
		pageData.setPassword(null);
		pageData.setPassword1(null);
		pageData.setPassword2(null);
		pageData.setLastUpdate(user.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeUser(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserPageData.class);
		User user = modelMapper.map(pageData, User.class);
		user = userService.close(user);
		mapUser(pageRequest, pageDto, user);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUser(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(
				pageRequest.getData().get(CommonConstants.FILTER).get(CommonConstants.COUNTRY_ID).asText());
		Page<User> userList = userService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UserPageData> list = mapUserData(userList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((userList.getNumber() + 1) == userList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<UserPageData> mapUserData(Page<User> page) {
		List<UserPageData> userPageDataList = new ArrayList<UserPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<User> pageDataList = page.getContent();
			for (User user : pageDataList) {
				UserPageData userPageData = modelMapper.map(user, UserPageData.class);
				userPageData.setLastUpdate(user.getOperationDateTime());
				userPageData.setPassword(null);
				userPageData.setPassword1(null);
				userPageData.setPassword2(null);
				userPageDataList.add(userPageData);
			}
		}
		return userPageDataList;
	}

	private void validateData(UserPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<UserPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}