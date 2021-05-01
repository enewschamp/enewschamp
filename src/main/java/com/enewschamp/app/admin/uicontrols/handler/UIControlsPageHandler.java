package com.enewschamp.app.admin.uicontrols.handler;

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
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("UIControlsPageHandler")
@Transactional
@Slf4j
public class UIControlsPageHandler implements IPageHandler {
	@Autowired
	private UIControlsService uiControlsService;
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
			pageDto = createUIControls(pageRequest);
			break;
		case "Update":
			pageDto = updateUIControls(pageRequest);
			break;
		case "Read":
			pageDto = readUIControls(pageRequest);
			break;
		case "Close":
			pageDto = closeUIControls(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateUIControls(pageRequest);
			break;
		case "List":
			pageDto = listUIControls(pageRequest);
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
	private PageDTO createUIControls(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				UIControlsPageData.class);
		validate(pageData);
		UIControls uiControls = mapUIControlsData(pageRequest, pageData);
		uiControls = uiControlsService.createOne(uiControls);
		mapUIControls(pageRequest, pageDto, uiControls);
		return pageDto;
	}

	private UIControls mapUIControlsData(PageRequestDTO pageRequest, UIControlsPageData pageData) {
		UIControls uiControls = modelMapper.map(pageData, UIControls.class);
		uiControls.setRecordInUse(RecordInUseType.Y);
		return uiControls;
	}

	@SneakyThrows
	private PageDTO updateUIControls(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				UIControlsPageData.class);
		validate(pageData);
		UIControls uiControls = mapUIControlsData(pageRequest, pageData);
		uiControls = uiControlsService.update(uiControls);
		mapUIControls(pageRequest, pageDto, uiControls);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readUIControls(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				UIControlsPageData.class);
		UIControls uiControls = modelMapper.map(pageData, UIControls.class);
		uiControls = uiControlsService.read(uiControls);
		mapUIControls(pageRequest, pageDto, uiControls);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeUIControls(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				UIControlsPageData.class);
		UIControls uiControls = modelMapper.map(pageData, UIControls.class);
		uiControls = uiControlsService.close(uiControls);
		mapUIControls(pageRequest, pageDto, uiControls);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateUIControls(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		UIControlsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				UIControlsPageData.class);
		UIControls uiControls = modelMapper.map(pageData, UIControls.class);
		uiControls = uiControlsService.reInstateUIControls(uiControls);
		mapUIControls(pageRequest, pageDto, uiControls);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listUIControls(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<UIControls> uiControlsList = uiControlsService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<UIControlsPageData> list = mapUIControlsData(uiControlsList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((uiControlsList.getNumber() + 1) == uiControlsList.getTotalPages()) {
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
		List<UIControlsPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<UIControlsPageData>>() {
				});
		List<UIControls> pageNavigators = mapUIControls(pageRequest, pageData);
		uiControlsService.clean();
		int totalRecords = uiControlsService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		mapHeaderData(pageRequest, pageDto);
		pageDto.setData(responseData);
		return pageDto;
	}

	private UIControlsPageData mapPageData(UIControls uiControls) {
		UIControlsPageData pageData = modelMapper.map(uiControls, UIControlsPageData.class);
		pageData.setLastUpdate(uiControls.getOperationDateTime());
		return pageData;
	}

	private void mapUIControls(PageRequestDTO pageRequest, PageDTO pageDto, UIControls uiControls) {
		UIControlsPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(uiControls);
		pageDto.setData(pageData);
	}

	public List<UIControlsPageData> mapUIControlsData(Page<UIControls> page) {
		List<UIControlsPageData> uiControlsPageDataList = new ArrayList<UIControlsPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<UIControls> pageDataList = page.getContent();
			for (UIControls uiControls : pageDataList) {
				UIControlsPageData uiControlsPageData = modelMapper.map(uiControls, UIControlsPageData.class);
				uiControlsPageData.setLastUpdate(uiControls.getOperationDateTime());
				uiControlsPageDataList.add(uiControlsPageData);
			}
		}
		return uiControlsPageDataList;
	}

	private void validate(UIControlsPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<UIControlsPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	private List<UIControls> mapUIControls(PageRequestDTO pageRequest, List<UIControlsPageData> pageData) {
		List<UIControls> pageNavigators = pageData.stream().peek(element -> {
			element.setOperatorId(pageRequest.getHeader().getUserId());
			element.setRecordInUse(RecordInUseType.Y);
		}).map(uiControls -> modelMapper.map(uiControls, UIControls.class)).collect(Collectors.toList());
		return pageNavigators;
	}
}