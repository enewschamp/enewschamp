package com.enewschamp.app.admin.handler;

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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
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
		mapHeaderData(pageRequest, pageDto, pageData, appSecurity);
		pageData.setAppSecId(appSecurity.getAppSecId());
		pageData.setLastUpdate(appSecurity.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, AppSecurityPageData pageData,
			AppSecurity appSecurity) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private AppSecurity mapAppSecurityData(PageRequestDTO pageRequest, AppSecurityPageData pageData) {
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity.setOperatorId(pageRequest.getData().get("operator").asText());
		appSecurity.setRecordInUse(RecordInUseType.Y);
		return appSecurity;
	}

	@SneakyThrows
	private PageDTO updateAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		validate(pageData);
		AppSecurity appSecurity = mapAppSecurityData(pageRequest, pageData);
		appSecurity = appSecurityService.update(appSecurity);
		mapHeaderData(pageRequest, pageDto, pageData, appSecurity);
		pageData.setAppSecId(appSecurity.getAppSecId());
		pageData.setLastUpdate(appSecurity.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity.setAppSecId(pageRequest.getData().get("id").asLong());
		appSecurity = appSecurityService.read(appSecurity);
		mapHeaderData(pageRequest, pageDto, pageData, appSecurity);
		mapPageData(pageData, appSecurity);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(AppSecurityPageData pageData, AppSecurity appSecurity) {
		pageData.setAppKey(appSecurity.getAppKey());
		pageData.setAppName(appSecurity.getAppName());
		pageData.setModule(appSecurity.getModule());
		pageData.setRecordInUse(appSecurity.getRecordInUse().toString());
		pageData.setOperator(appSecurity.getOperatorId());
		pageData.setLastUpdate(appSecurity.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeAppSecurity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		AppSecurityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				AppSecurityPageData.class);
		AppSecurity appSecurity = modelMapper.map(pageData, AppSecurity.class);
		appSecurity.setAppSecId(pageData.getId());
		appSecurity = appSecurityService.close(appSecurity);
		mapHeaderData(pageRequest, pageDto, pageData, appSecurity);
		mapPageData(pageData, appSecurity);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listAppSecurity(PageRequestDTO pageRequest) {
		Page<AppSecurity> appSecList = appSecurityService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<AppSecurityPageData> list = mapAppSecurityData(appSecList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((appSecList.getNumber() + 1) == appSecList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
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
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				AppSecurityPageData appSecPageData = modelMapper.map(appSecurity, AppSecurityPageData.class);
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
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
