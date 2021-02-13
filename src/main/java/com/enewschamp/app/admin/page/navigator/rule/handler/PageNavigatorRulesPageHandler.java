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
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.bulk.handler.BulkInsertResponsePageData;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("PageNavigatorRulesPageHandler")
@Slf4j
@Transactional
public class PageNavigatorRulesPageHandler implements IPageHandler {
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
		case "Insert":
			pageDto = insertAll(pageRequest);
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
		PageNavigatorRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorRulesPageData.class);
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
			PageNavigatorRulesPageData pageData) {
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator.setRecordInUse(RecordInUseType.Y);
		return pageNavigator;
	}

	@SneakyThrows
	private PageDTO updatePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorRulesPageData.class);
		validateData(pageData);
		PageNavigatorRules pageNavigator = mapPageNavigatorRulesData(pageRequest, pageData);
		pageNavigator = pageNavigationRulesService.update(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private void mapPageNavigatorRules(PageRequestDTO pageRequest, PageDTO pageDto, PageNavigatorRules pageNavigator) {
		PageNavigatorRulesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(pageNavigator);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readPageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.read(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstatePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.reInstate(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private PageNavigatorRulesPageData mapPageData(PageNavigatorRules pageNavigator) {
		PageNavigatorRulesPageData pageData = modelMapper.map(pageNavigator, PageNavigatorRulesPageData.class);
		pageData.setLastUpdate(pageNavigator.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closePageNavigatorRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorRulesPageData.class);
		PageNavigatorRules pageNavigator = modelMapper.map(pageData, PageNavigatorRules.class);
		pageNavigator = pageNavigationRulesService.close(pageNavigator);
		mapPageNavigatorRules(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPageNavigatorRules(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<PageNavigatorRules> pageNavigatorList = pageNavigationRulesService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PageNavigatorRulesPageData> list = mapPageNavigatorRulesData(pageNavigatorList);
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
	
	@SneakyThrows
	@Transactional
	private PageDTO insertAll(PageRequestDTO pageRequest) {
	    PageDTO pageDto = new PageDTO();
		List<PageNavigatorRulesPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<PageNavigatorRulesPageData>>() {
				});
		List<PageNavigatorRules> pageNavigators = mapPageNavigatorRules(pageRequest, pageData);
		pageNavigationRulesService.clean();
		int totalRecords = pageNavigationRulesService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.setData(responseData);
		return pageDto;
	}


	private List<PageNavigatorRulesPageData> mapPageNavigatorRulesData(Page<PageNavigatorRules> page) {
		List<PageNavigatorRulesPageData> pageNavigatorPageDataList = new ArrayList<PageNavigatorRulesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<PageNavigatorRules> pageDataList = page.getContent();
			for (PageNavigatorRules pageNavigator : pageDataList) {
				PageNavigatorRulesPageData pageNavigatorPageData = modelMapper.map(pageNavigator,
						PageNavigatorRulesPageData.class);
				pageNavigatorPageData.setLastUpdate(pageNavigator.getOperationDateTime());
				pageNavigatorPageDataList.add(pageNavigatorPageData);
			}
		}
		return pageNavigatorPageDataList;
	}

	private void validateData(PageNavigatorRulesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PageNavigatorRulesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
	
	private List<PageNavigatorRules> mapPageNavigatorRules(PageRequestDTO pageRequest, List<PageNavigatorRulesPageData> pageData) {
		List<PageNavigatorRules> pageNavigators = pageData.stream()
				.map(navigator -> modelMapper.map(navigator, PageNavigatorRules.class)).collect(Collectors.toList());
		return pageNavigators;
	}


}