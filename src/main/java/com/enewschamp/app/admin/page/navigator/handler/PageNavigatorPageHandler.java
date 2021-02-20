package com.enewschamp.app.admin.page.navigator.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.bulk.handler.BulkInsertResponsePageData;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.page.navigator.service.PageNavigatorService;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("PageNavigatorPageHandler")
@Transactional
public class PageNavigatorPageHandler implements IPageHandler {
	@Autowired
	private PageNavigatorService pageNavigatorService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createPageNavigator(pageRequest);
			break;
		case "Update":
			pageDto = updatePageNavigator(pageRequest);
			break;
		case "Read":
			pageDto = readPageNavigator(pageRequest);
			break;
		case "Close":
			pageDto = closePageNavigator(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstatePageNavigator(pageRequest);
			break;
		case "List":
			pageDto = listPageNavigator(pageRequest);
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
	private PageDTO createPageNavigator(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorPageData.class);
		validate(pageData, this.getClass().getName());
		PageNavigator pageNavigator = mapPageNavigatorData(pageRequest, pageData);
		pageNavigator = pageNavigatorService.create(pageNavigator);
		mapPageNavigator(pageRequest, pageDto, pageNavigator);
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

	private PageNavigator mapPageNavigatorData(PageRequestDTO pageRequest, PageNavigatorPageData pageData) {
		PageNavigator pageNavigator = modelMapper.map(pageData, PageNavigator.class);
		pageNavigator.setRecordInUse(RecordInUseType.Y);
		return pageNavigator;
	}

	@SneakyThrows
	private PageDTO updatePageNavigator(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorPageData.class);
		validate(pageData, this.getClass().getName());
		PageNavigator pageNavigator = mapPageNavigatorData(pageRequest, pageData);
		pageNavigator = pageNavigatorService.update(pageNavigator);
		mapPageNavigator(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private void mapPageNavigator(PageRequestDTO pageRequest, PageDTO pageDto, PageNavigator pageNavigator) {
		PageNavigatorPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(pageNavigator);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readPageNavigator(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorPageData.class);
		PageNavigator pageNavigator = modelMapper.map(pageData, PageNavigator.class);
		pageNavigator = pageNavigatorService.read(pageNavigator);
		mapPageNavigator(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstatePageNavigator(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorPageData.class);
		PageNavigator pageNavigator = modelMapper.map(pageData, PageNavigator.class);
		pageNavigator = pageNavigatorService.reInstate(pageNavigator);
		mapPageNavigator(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	private PageNavigatorPageData mapPageData(PageNavigator pageNavigator) {
		PageNavigatorPageData pageData = modelMapper.map(pageNavigator, PageNavigatorPageData.class);
		pageData.setLastUpdate(pageNavigator.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closePageNavigator(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PageNavigatorPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PageNavigatorPageData.class);
		PageNavigator pageNavigator = modelMapper.map(pageData, PageNavigator.class);
		pageNavigator = pageNavigatorService.close(pageNavigator);
		mapPageNavigator(pageRequest, pageDto, pageNavigator);
		return pageDto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO insertAll(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		List<PageNavigatorPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<PageNavigatorPageData>>() {
				});
		pageData.forEach(pagedata ->{
			validate(pageData, this.getClass().getName());
		});
		List<PageNavigator> pageNavigators = mapPageNavigators(pageRequest, pageData);
		pageNavigatorService.clean();
		int totalRecords = pageNavigatorService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.setData(responseData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPageNavigator(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<PageNavigator> pageNavigatorList = pageNavigatorService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PageNavigatorPageData> list = mapPageNavigatorData(pageNavigatorList);
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

	private List<PageNavigatorPageData> mapPageNavigatorData(Page<PageNavigator> page) {
		List<PageNavigatorPageData> pageNavigatorPageDataList = new ArrayList<PageNavigatorPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<PageNavigator> pageDataList = page.getContent();
			for (PageNavigator pageNavigator : pageDataList) {
				PageNavigatorPageData pageNavigatorPageData = modelMapper.map(pageNavigator,
						PageNavigatorPageData.class);
				pageNavigatorPageData.setLastUpdate(pageNavigator.getOperationDateTime());
				pageNavigatorPageDataList.add(pageNavigatorPageData);
			}
		}
		return pageNavigatorPageDataList;
	}

	private List<PageNavigator> mapPageNavigators(PageRequestDTO pageRequest, List<PageNavigatorPageData> pageData) {
		List<PageNavigator> pageNavigators = pageData.stream()
				.map(navigator -> modelMapper.map(navigator, PageNavigator.class)).collect(Collectors.toList());
		return pageNavigators;
	}
}
