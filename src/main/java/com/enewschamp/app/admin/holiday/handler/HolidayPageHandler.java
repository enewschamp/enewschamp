package com.enewschamp.app.admin.holiday.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("HolidayPageHandler")
public class HolidayPageHandler implements IPageHandler {
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createHoliday(pageRequest);
			break;
		case "Update":
			pageDto = updateHoliday(pageRequest);
			break;
		case "Read":
			pageDto = readHoliday(pageRequest);
			break;
		case "Close":
			pageDto = closeHoliday(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateHoliday(pageRequest);
			break;
		case "List":
			pageDto = listHoliday(pageRequest);
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
	private PageDTO createHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		validate(pageData, this.getClass().getName());
		Holiday holiday = mapHolidayData(pageRequest, pageData);
		holiday = holidayService.create(holiday);
		mapHoliday(pageRequest, pageDto, holiday);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		validate(pageData, this.getClass().getName());
		Holiday holiday = mapHolidayData(pageRequest, pageData);
		holiday = holidayService.update(holiday);
		mapHoliday(pageRequest, pageDto, holiday);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday = holidayService.read(holiday);
		mapHoliday(pageRequest, pageDto, holiday);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday = holidayService.close(holiday);
		mapHoliday(pageRequest, pageDto, holiday);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday = holidayService.reinstate(holiday);
		mapHoliday(pageRequest, pageDto, holiday);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listHoliday(PageRequestDTO pageRequest) {
		Page<Holiday> editionList = holidayService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<HolidayPageData> list = mapHolidayData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<HolidayPageData> mapHolidayData(Page<Holiday> page) {
		List<HolidayPageData> holidayPageDataList = new ArrayList<HolidayPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Holiday> pageDataList = page.getContent();
			for (Holiday holiday : pageDataList) {
				HolidayPageData holidayPageData = modelMapper.map(holiday, HolidayPageData.class);
				holidayPageData.setLastUpdate(holiday.getOperationDateTime());
				holidayPageDataList.add(holidayPageData);
			}
		}
		return holidayPageDataList;
	}

	private void mapHoliday(PageRequestDTO pageRequest, PageDTO pageDto, Holiday holiday) {
		HolidayPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(holiday);
		pageDto.setData(pageData);
	}

	private Holiday mapHolidayData(PageRequestDTO pageRequest, HolidayPageData pageData) {
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday.setRecordInUse(RecordInUseType.Y);
		return holiday;
	}

	private HolidayPageData mapPageData(Holiday holiday) {
		HolidayPageData pageData = modelMapper.map(holiday, HolidayPageData.class);
		pageData.setLastUpdate(holiday.getOperationDateTime());
		return pageData;
	}
}
