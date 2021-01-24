package com.enewschamp.app.admin.user.leave.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.enewschamp.user.domain.entity.UserLeave;
import com.enewschamp.user.domain.entity.UserLeaveKey;
import com.enewschamp.user.domain.service.UserLeaveService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UserLeavePageHandler")
@Slf4j
public class UserLeavePageHandler implements IPageHandler {
	@Autowired
	private UserLeaveService userLeaveService;
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
			pageDto = createUserLeave(pageRequest);
			break;
		case "Update":
			pageDto = updateUserLeave(pageRequest);
			break;
		case "Read":
			pageDto = readUserLeave(pageRequest);
			break;
		case "Close":
			pageDto = closeUserLeave(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInstateuserLeave(pageRequest);
			break;
		case "List":
			pageDto = listUserLeave(pageRequest);
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
	private PageDTO createUserLeave(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLeavePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLeavePageData.class);
		validateData(pageData);
		UserLeave userLeave = mapUserLeaveData(pageRequest, pageData);
		if (pageData.getUpdateApplicationDateTime() != null && pageData.getUpdateApplicationDateTime().equals("Y"))
			userLeave.setApplicationDateTime(LocalDateTime.now());
		userLeave = userLeaveService.create(userLeave);
		mapUserLeave(pageRequest, pageDto, userLeave);
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

	private UserLeave mapUserLeaveData(PageRequestDTO pageRequest, UserLeavePageData pageData) {
		UserLeave userLeave = modelMapper.map(pageData, UserLeave.class);
		mapLeaveKeys(pageData, userLeave);
		userLeave.setRecordInUse(RecordInUseType.Y);
		return userLeave;
	}

	@SneakyThrows
	private PageDTO updateUserLeave(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLeavePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLeavePageData.class);
		validateData(pageData);
		UserLeave userLeave = mapUserLeaveData(pageRequest, pageData);
		if (pageData.getUpdateApplicationDateTime() != null && pageData.getUpdateApplicationDateTime().equals("Y"))
			userLeave.setApplicationDateTime(LocalDateTime.now());
		userLeave = userLeaveService.update(userLeave);
		mapUserLeave(pageRequest, pageDto, userLeave);
		return pageDto;
	}

	private void mapUserLeave(PageRequestDTO pageRequest, PageDTO pageDto, UserLeave userLeave) {
		UserLeavePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(userLeave);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readUserLeave(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLeavePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLeavePageData.class);
		UserLeave userLeave = modelMapper.map(pageData, UserLeave.class);
		mapLeaveKeys(pageData, userLeave);
		userLeave = userLeaveService.read(userLeave);
		mapUserLeave(pageRequest, pageDto, userLeave);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateuserLeave(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLeavePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLeavePageData.class);
		UserLeave userLeave = modelMapper.map(pageData, UserLeave.class);
		if (pageData.getUpdateApplicationDateTime() != null && pageData.getUpdateApplicationDateTime().equals("Y"))
			userLeave.setApplicationDateTime(LocalDateTime.now());
		mapLeaveKeys(pageData, userLeave);
		userLeave = userLeaveService.reInstate(userLeave);
		mapUserLeave(pageRequest, pageDto, userLeave);
		return pageDto;
	}

	private void mapLeaveKeys(UserLeavePageData pageData, UserLeave userLeave) {
		UserLeaveKey userLeaveKey = new UserLeaveKey();
		userLeaveKey.setStartDate(pageData.getStartDate());
		userLeaveKey.setUserId(pageData.getUserId());
		userLeave.setUserLeaveKey(userLeaveKey);
	}

	private UserLeavePageData mapPageData(UserLeave userLeave) {
		UserLeavePageData pageData = modelMapper.map(userLeave, UserLeavePageData.class);
		pageData.setUserId(userLeave.getUserLeaveKey().getUserId());
		pageData.setStartDate(userLeave.getUserLeaveKey().getStartDate());
		pageData.setLastUpdate(userLeave.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeUserLeave(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserLeavePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserLeavePageData.class);
		UserLeave userLeave = modelMapper.map(pageData, UserLeave.class);
		if (pageData.getUpdateApplicationDateTime() != null && pageData.getUpdateApplicationDateTime().equals("Y"))
			userLeave.setApplicationDateTime(LocalDateTime.now());
		mapLeaveKeys(pageData, userLeave);
		userLeave = userLeaveService.close(userLeave);
		mapUserLeave(pageRequest, pageDto, userLeave);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUserLeave(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UserLeave> userLeaveList = userLeaveService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UserLeavePageData> list = mapUserLeaveData(userLeaveList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((userLeaveList.getNumber() + 1) == userLeaveList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<UserLeavePageData> mapUserLeaveData(Page<UserLeave> page) {
		List<UserLeavePageData> userLeavePageDataList = new ArrayList<UserLeavePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UserLeave> pageDataList = page.getContent();
			for (UserLeave userLeave : pageDataList) {
				UserLeavePageData userLeavePageData = modelMapper.map(userLeave, UserLeavePageData.class);
				userLeavePageData.setUserId(userLeave.getUserLeaveKey().getUserId());
				userLeavePageData.setStartDate(userLeave.getUserLeaveKey().getStartDate());
				userLeavePageData.setLastUpdate(userLeave.getOperationDateTime());
				userLeavePageDataList.add(userLeavePageData);
			}
		}
		return userLeavePageDataList;
	}

	private void validateData(UserLeavePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<UserLeavePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}