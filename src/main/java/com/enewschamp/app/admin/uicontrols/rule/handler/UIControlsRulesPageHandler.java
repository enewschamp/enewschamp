package com.enewschamp.app.admin.uicontrols.rule.handler;

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
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
import com.enewschamp.app.common.uicontrols.service.UIControlsRulesService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UIControlsRulesPageHandler")
@Slf4j
public class UIControlsRulesPageHandler implements IPageHandler {
	@Autowired
	private UIControlsRulesService uiControlsRuleService;
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
			pageDto = createUIControlsRules(pageRequest);
			break;
		case "Update":
			pageDto = updateUIControlsRules(pageRequest);
			break;
		case "Read":
			pageDto = readUIControlsRules(pageRequest);
			break;
		case "Close":
			pageDto = closeUIControlsRules(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateUIControlsRules(pageRequest);
			break;
		case "List":
			pageDto = listUIControlsRules(pageRequest);
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
	private PageDTO createUIControlsRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsRulesPageData.class);
		validate(pageData);
		UIControlsRules uiControlesRulesGlobal = mapUIControlsRulesData(pageRequest, pageData);
		uiControlesRulesGlobal = uiControlsRuleService.create(uiControlesRulesGlobal);
		mapUIControlsRules(pageRequest, pageDto, uiControlesRulesGlobal);
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

	private UIControlsRules mapUIControlsRulesData(PageRequestDTO pageRequest, UIControlsRulesPageData pageData) {
		UIControlsRules uiControlesRules = modelMapper.map(pageData, UIControlsRules.class);
		uiControlesRules.setRecordInUse(RecordInUseType.Y);
		return uiControlesRules;
	}

	@SneakyThrows
	private PageDTO updateUIControlsRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsRulesPageData.class);
		validate(pageData);
		UIControlsRules uiControlesRules = mapUIControlsRulesData(pageRequest, pageData);
		uiControlesRules = uiControlsRuleService.update(uiControlesRules);
		mapUIControlsRules(pageRequest, pageDto, uiControlesRules);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readUIControlsRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsRulesPageData.class);
		UIControlsRules uiControlesRules = modelMapper.map(pageData, UIControlsRules.class);
		uiControlesRules = uiControlsRuleService.read(uiControlesRules);
		mapUIControlsRules(pageRequest, pageDto, uiControlesRules);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeUIControlsRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsRulesPageData.class);
		UIControlsRules uiControlesRules = modelMapper.map(pageData, UIControlsRules.class);
		uiControlesRules = uiControlsRuleService.close(uiControlesRules);
		mapUIControlsRules(pageRequest, pageDto, uiControlesRules);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateUIControlsRules(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsRulesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsRulesPageData.class);
		UIControlsRules uiControlesRules = modelMapper.map(pageData, UIControlsRules.class);
		uiControlesRules = uiControlsRuleService.reinstate(uiControlesRules);
		mapUIControlsRules(pageRequest, pageDto, uiControlesRules);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUIControlsRules(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UIControlsRules> uiControlesRulesList = uiControlsRuleService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UIControlsRulesPageData> list = mapUIControlsRulesData(uiControlesRulesList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((uiControlesRulesList.getNumber() + 1) == uiControlesRulesList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private UIControlsRulesPageData mapPageData(UIControlsRules uiControlesRules) {
		UIControlsRulesPageData pageData = modelMapper.map(uiControlesRules, UIControlsRulesPageData.class);
		pageData.setLastUpdate(uiControlesRules.getOperationDateTime());
		return pageData;
	}

	private void mapUIControlsRules(PageRequestDTO pageRequest, PageDTO pageDto, UIControlsRules uiControlesRules) {
		UIControlsRulesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(uiControlesRules);
		pageDto.setData(pageData);
	}

	public List<UIControlsRulesPageData> mapUIControlsRulesData(Page<UIControlsRules> page) {
		List<UIControlsRulesPageData> uiControlesRulesPageDataList = new ArrayList<UIControlsRulesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UIControlsRules> pageDataList = page.getContent();
			for (UIControlsRules uiControlesRules : pageDataList) {
				UIControlsRulesPageData uiControlesRulesPageData = modelMapper.map(uiControlesRules, UIControlsRulesPageData.class);
				uiControlesRulesPageData.setLastUpdate(uiControlesRules.getOperationDateTime());
				uiControlesRulesPageDataList.add(uiControlesRulesPageData);
			}
		}
		return uiControlesRulesPageDataList;
	}

	private void validate(UIControlsRulesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<UIControlsRulesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}