package com.enewschamp.app.admin.uicontrols.global.handler;

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
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;
import com.enewschamp.app.common.uicontrols.service.UIControlsGlobalService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UIControlsGlobalPageHandler")
@Slf4j
public class UIControlsGlobalPageHandler implements IPageHandler {
	@Autowired
	private UIControlsGlobalService uiControlsGlobalGlobalsService;
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
			pageDto = createUIControlsGlobal(pageRequest);
			break;
		case "Update":
			pageDto = updateUIControlsGlobal(pageRequest);
			break;
		case "Read":
			pageDto = readUIControlsGlobal(pageRequest);
			break;
		case "Close":
			pageDto = closeUIControlsGlobal(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateUIControlsGlobal(pageRequest);
			break;
		case "List":
			pageDto = listUIControlsGlobal(pageRequest);
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
	private PageDTO createUIControlsGlobal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsGlobalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsGlobalPageData.class);
		validate(pageData);
		UIControlsGlobal uiControlsGlobalGlobal = mapUIControlsGlobalData(pageRequest, pageData);
		uiControlsGlobalGlobal = uiControlsGlobalGlobalsService.createOne(uiControlsGlobalGlobal);
		mapUIControlsGlobal(pageRequest, pageDto, uiControlsGlobalGlobal);
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

	private UIControlsGlobal mapUIControlsGlobalData(PageRequestDTO pageRequest, UIControlsGlobalPageData pageData) {
		UIControlsGlobal uiControlsGlobal = modelMapper.map(pageData, UIControlsGlobal.class);
		uiControlsGlobal.setRecordInUse(RecordInUseType.Y);
		return uiControlsGlobal;
	}

	@SneakyThrows
	private PageDTO updateUIControlsGlobal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsGlobalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsGlobalPageData.class);
		validate(pageData);
		UIControlsGlobal uiControlsGlobal = mapUIControlsGlobalData(pageRequest, pageData);
		uiControlsGlobal = uiControlsGlobalGlobalsService.update(uiControlsGlobal);
		mapUIControlsGlobal(pageRequest, pageDto, uiControlsGlobal);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readUIControlsGlobal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsGlobalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsGlobalPageData.class);
		UIControlsGlobal uiControlsGlobal = modelMapper.map(pageData, UIControlsGlobal.class);
		uiControlsGlobal = uiControlsGlobalGlobalsService.read(uiControlsGlobal);
		mapUIControlsGlobal(pageRequest, pageDto, uiControlsGlobal);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeUIControlsGlobal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsGlobalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsGlobalPageData.class);
		UIControlsGlobal uiControlsGlobal = modelMapper.map(pageData, UIControlsGlobal.class);
		uiControlsGlobal = uiControlsGlobalGlobalsService.close(uiControlsGlobal);
		mapUIControlsGlobal(pageRequest, pageDto, uiControlsGlobal);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateUIControlsGlobal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsGlobalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), UIControlsGlobalPageData.class);
		UIControlsGlobal uiControlsGlobal = modelMapper.map(pageData, UIControlsGlobal.class);
		uiControlsGlobal = uiControlsGlobalGlobalsService.reinstate(uiControlsGlobal);
		mapUIControlsGlobal(pageRequest, pageDto, uiControlsGlobal);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUIControlsGlobal(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UIControlsGlobal> uiControlsGlobalList = uiControlsGlobalGlobalsService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UIControlsGlobalPageData> list = mapUIControlsGlobalData(uiControlsGlobalList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((uiControlsGlobalList.getNumber() + 1) == uiControlsGlobalList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}
	
	@SneakyThrows
	@Transactional
	private PageDTO insertAll(PageRequestDTO pageRequest) {
		// PageDTO pageDto = new PageDTO();
		List<UIControlsGlobalPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<UIControlsGlobalPageData>>() {
				});
		List<UIControlsGlobal> uiControlsGlobals = mapUIControlsGlobals(pageRequest, pageData);
		uiControlsGlobalGlobalsService.createAll(uiControlsGlobals);
		return null;
	}

	private UIControlsGlobalPageData mapPageData(UIControlsGlobal uiControlsGlobal) {
		UIControlsGlobalPageData pageData = modelMapper.map(uiControlsGlobal, UIControlsGlobalPageData.class);
		pageData.setLastUpdate(uiControlsGlobal.getOperationDateTime());
		return pageData;
	}

	private void mapUIControlsGlobal(PageRequestDTO pageRequest, PageDTO pageDto, UIControlsGlobal uiControlsGlobal) {
		UIControlsGlobalPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(uiControlsGlobal);
		pageDto.setData(pageData);
	}

	public List<UIControlsGlobalPageData> mapUIControlsGlobalData(Page<UIControlsGlobal> page) {
		List<UIControlsGlobalPageData> uiControlsGlobalPageDataList = new ArrayList<UIControlsGlobalPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UIControlsGlobal> pageDataList = page.getContent();
			for (UIControlsGlobal uiControlsGlobal : pageDataList) {
				UIControlsGlobalPageData uiControlsGlobalPageData = modelMapper.map(uiControlsGlobal, UIControlsGlobalPageData.class);
				uiControlsGlobalPageData.setLastUpdate(uiControlsGlobal.getOperationDateTime());
				uiControlsGlobalPageDataList.add(uiControlsGlobalPageData);
			}
		}
		return uiControlsGlobalPageDataList;
	}

	private void validate(UIControlsGlobalPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<UIControlsGlobalPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
	
	private List<UIControlsGlobal> mapUIControlsGlobals(PageRequestDTO pageRequest, List<UIControlsGlobalPageData> pageData) {
		List<UIControlsGlobal> pageNavigators = pageData.stream()
				.map(uicontrolglobal -> modelMapper.map(uicontrolglobal, UIControlsGlobal.class)).collect(Collectors.toList());
		return pageNavigators;
	}
}
