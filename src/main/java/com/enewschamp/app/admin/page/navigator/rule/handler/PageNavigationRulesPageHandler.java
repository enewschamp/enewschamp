package com.enewschamp.app.admin.page.navigator.rule.handler;

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
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationRulesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("PageNavigationRulesPageHandler")
@Slf4j
public class PageNavigationRulesPageHandler implements IPageHandler {
	@Autowired
	private PageNavigationRulesService pageNavigationRulesService;
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
			pageDto = createPageNavigatorRules(pageRequest);
			break;
		case "Update":
			pageDto = updatePageNavigatorRules(pageRequest);
			break;
		case "Read":
			pageDto = readPageNavigatorRules(pageRequest);
			break;
		case "Close":
			pageDto = closePageNavigatorRules(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstatePageNavigatorRules(pageRequest);
			break;
		case "List":
			pageDto = listPageNavigatorRules(pageRequest);
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
	private PageDTO createPageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigationRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigationRulesPageData.class);
		validateData(pageData);
		PageNavigatorRules pageNavigator = mapPageNavigatorRulesData(pageRequest, pageData);
		pageNavigator = pageNavigationRulesService.create(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
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

	private PageNavigatorRules mapPageNavigatorRulesData(PageRequestDTO pageRequest,
			PageNavigationRulesPageData pageData) {
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator.setRecordInUse(RecordInUseType.Y);
		return pageNavigator;
	}

	@SneakyThrows
	private PageDTO updatePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigationRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigationRulesPageData.class);
		validateData(pageData);
		PageNavigatorRules pageNavigator = mapPageNavigatorRulesData(pageRequest, pageData);
		pageNavigator = pageNavigationRulesService.update(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private void mapPageNavigatorRules(PageRequestDTO pageRequest, PageDTO pageDto, PageNavigatorRules pageNavigator) {
		PageNavigationRulesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(pageNavigator);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readPageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigationRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigationRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.read(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstatePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigationRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigationRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.reInstate(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private PageNavigationRulesPageData mapPageData(PageNavigatorRules pageNavigator) {
		PageNavigationRulesPageData pageData = modelMapper.map(pageNavigator, PageNavigationRulesPageData.class);
		pageData.setLastUpdate(pageNavigator.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigationRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigationRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.close(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPageNavigatorRules(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(
				pageRequest.getData().get(CommonConstants.FILTER).get(CommonConstants.COUNTRY_ID).asText());
		Page<PageNavigatorRules> pageNavigatorList = pageNavigationRulesService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PageNavigationRulesPageData> list = mapPageNavigatorRulesData(pageNavigatorList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((pageNavigatorList.getNumber() + 1) == pageNavigatorList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<PageNavigationRulesPageData> mapPageNavigatorRulesData(Page<PageNavigatorRules> page) {
		List<PageNavigationRulesPageData> pageNavigatorPageDataList = new ArrayList<PageNavigationRulesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<PageNavigatorRules> pageDataList = page.getContent();
			for (PageNavigatorRules pageNavigator : pageDataList) {
				PageNavigationRulesPageData pageNavigatorPageData = modelMapper.map(pageNavigator,
						PageNavigationRulesPageData.class);
				pageNavigatorPageData.setLastUpdate(pageNavigator.getOperationDateTime());
				pageNavigatorPageDataList.add(pageNavigatorPageData);
			}
		}
		return pageNavigatorPageDataList;
	}

	private void validateData(PageNavigationRulesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PageNavigationRulesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}


}