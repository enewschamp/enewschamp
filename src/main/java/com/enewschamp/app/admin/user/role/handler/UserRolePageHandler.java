package com.enewschamp.app.admin.user.role.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;
import com.enewschamp.user.domain.service.UserRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("UserRolePageHandler")
public class UserRolePageHandler implements IPageHandler {
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createUserRole(pageRequest);
			break;
		case "Update":
			pageDto = updateUserRole(pageRequest);
			break;
		case "Read":
			pageDto = readUserRole(pageRequest);
			break;
		case "Close":
			pageDto = closeUserRole(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateUserRole(pageRequest);
			break;
		case "List":
			pageDto = listUserRole(pageRequest);
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
	private PageDTO createUserRole(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserRolePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserRolePageData.class);
		validate(pageData,  this.getClass().getName());
		UserRole userRole = mapUserRoleData(pageRequest, pageData);
		userRole = userRoleService.create(userRole);
		mapUserRole(pageRequest, pageDto, userRole);
		return pageDto;
	}

	private UserRole mapUserRoleData(PageRequestDTO pageRequest, UserRolePageData pageData) {
		UserRole userRole = modelMapper.map(pageData, UserRole.class);
		mapRoleKey(pageData, userRole);
		userRole.setRecordInUse(RecordInUseType.Y);

		return userRole;
	}

	@SneakyThrows
	private PageDTO updateUserRole(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserRolePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserRolePageData.class);
		validate(pageData,  this.getClass().getName());
		UserRole userRole = mapUserRoleData(pageRequest, pageData);
		userRole = userRoleService.update(userRole);
		mapUserRole(pageRequest, pageDto, userRole);
		return pageDto;
	}

	private void mapUserRole(PageRequestDTO pageRequest, PageDTO pageDto, UserRole userRole) {
		UserRolePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(userRole);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readUserRole(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserRolePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserRolePageData.class);
		UserRole userRole = modelMapper.map(pageData, UserRole.class);
		mapRoleKey(pageData, userRole);
		userRole = userRoleService.read(userRole);
		mapUserRole(pageRequest, pageDto, userRole);
		return pageDto;
	}

	private void mapRoleKey(UserRolePageData pageData, UserRole userRole) {
		UserRoleKey roleKey = new UserRoleKey();
		roleKey.setRoleId(pageData.getRoleId());
		roleKey.setUserId(pageData.getUserId());
		roleKey.setDayOfTheWeek(pageData.getDayOfTheWeek());
		userRole.setUserRoleKey(roleKey);
	}

	@SneakyThrows
	private PageDTO reinstateUserRole(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserRolePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserRolePageData.class);
		UserRole userRole = modelMapper.map(pageData, UserRole.class);
		mapRoleKey(pageData, userRole);
		userRole = userRoleService.reInstate(userRole);
		mapUserRole(pageRequest, pageDto, userRole);
		return pageDto;
	}

	private UserRolePageData mapPageData(UserRole userRole) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserRolePageData pageData = modelMapper.map(userRole, UserRolePageData.class);
		pageData.setRoleId(userRole.getUserRoleKey().getRoleId());
		pageData.setUserId(userRole.getUserRoleKey().getUserId());
		pageData.setDayOfTheWeek(userRole.getUserRoleKey().getDayOfTheWeek());
		pageData.setLastUpdate(userRole.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeUserRole(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UserRolePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UserRolePageData.class);
		UserRole userRole = modelMapper.map(pageData, UserRole.class);
		mapRoleKey(pageData, userRole);
		userRole = userRoleService.close(userRole);
		mapUserRole(pageRequest, pageDto, userRole);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUserRole(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UserRole> userRoleList = userRoleService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UserRolePageData> list = mapUserRoleData(userRoleList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((userRoleList.getNumber() + 1) == userRoleList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<UserRolePageData> mapUserRoleData(Page<UserRole> page) {
		List<UserRolePageData> userRolePageDataList = new ArrayList<UserRolePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UserRole> pageDataList = page.getContent();
			for (UserRole userRole : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				UserRolePageData userRolePageData = modelMapper.map(userRole, UserRolePageData.class);
				userRolePageData.setRoleId(userRole.getUserRoleKey().getRoleId());
				userRolePageData.setUserId(userRole.getUserRoleKey().getUserId());
				userRolePageData.setLastUpdate(userRole.getOperationDateTime());
				userRolePageData.setDayOfTheWeek(userRole.getUserRoleKey().getDayOfTheWeek());
				userRolePageDataList.add(userRolePageData);
			}
		}
		return userRolePageDataList;
	}
	
}