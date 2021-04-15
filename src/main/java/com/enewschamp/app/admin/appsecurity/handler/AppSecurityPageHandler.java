package com.enewschamp.app.admin.appsecurity.handler;

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

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.entity.AppSecurity;
import com.enewschamp.security.service.AppSecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("AppSecurityPageHandler")
@Slf4j
public class AppSecurityPageHandler implements IPageHandler {
	@Autowired
	private AppSecurityService appSecurityService;
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
			pageDto = createAppSecurity(pageRequest);
			break;
		case "Update":
			pageDto = updateAppSecurity(pageRequest);
			break;
		case "Read":
			pageDto = readAppSecurity(pageRequest);
			break;
		case "Close":
			pageDto = closeAppSecurity(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateAppSecurity(pageRequest);
			break;
		case "List":
			pageDto = listAppSecurity(pageRequest);
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
	private PageDTO createAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		validate(pageData);
		AppSecurity appSecurity = mapAppSecurityData(pageRequest, pageData);
		appSecurity = appSecurityService.create(appSecurity);
		mapAppSecurity(pageRequest, pageDto, appSecurity);
		return pageDto;
	}

	private AppSecurity mapAppSecurityData(PageRequestDTO pageRequest, AppSecurityPageData pageData) {
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity.setRecordInUse(RecordInUseType.Y);
		return appSecurity;
	}

	private void mapAppSecurity(PageRequestDTO pageRequest, PageDTO pageDto, AppSecurity appSecurity) {
		AppSecurityPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(appSecurity);
		pageDto.setData(pageData);
	}
	@SneakyThrows
	private PageDTO updateAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		validate(pageData);
		AppSecurity appSecurity = mapAppSecurityData(pageRequest, pageData);
		appSecurity = appSecurityService.update(appSecurity);
		mapAppSecurity(pageRequest, pageDto, appSecurity);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity = appSecurityService.read(appSecurity);
		mapAppSecurity(pageRequest, pageDto, appSecurity);
		return pageDto;
	}

	
	private AppSecurityPageData mapPageData(AppSecurity appSecurity) {
		AppSecurityPageData pageData = modelMapper.map(appSecurity, AppSecurityPageData.class);
		pageData.setLastUpdate(appSecurity.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity = appSecurityService.close(appSecurity);
		mapAppSecurity(pageRequest, pageDto, appSecurity);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity = appSecurityService.reinstate(appSecurity);
		mapAppSecurity(pageRequest, pageDto, appSecurity);
		return pageDto;
	}
	@SneakyThrows
	private PageDTO listAppSecurity(PageRequestDTO pageRequest) {
		Page<AppSecurity> appSecList = appSecurityService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<AppSecurityPageData> list = mapAppSecurityData(appSecList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((appSecList.getNumber() + 1) == appSecList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<AppSecurityPageData> mapAppSecurityData(Page<AppSecurity> page) {
		List<AppSecurityPageData> countryPageDataList = new ArrayList<AppSecurityPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<AppSecurity> pageDataList = page.getContent();
			for (AppSecurity appSecurity : pageDataList) {
				AppSecurityPageData appSecPageData = modelMapper.map(appSecurity, AppSecurityPageData.class);
			//	appSecPageData.setLastUpdate(appSecurity.getOperationDateTime());
				countryPageDataList.add(appSecPageData);
			}
		}
		return countryPageDataList;
	}

	private void validate(AppSecurityPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<AppSecurityPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
