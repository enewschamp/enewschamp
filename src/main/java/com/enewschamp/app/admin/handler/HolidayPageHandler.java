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
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("HolidayPageHandler")
@Slf4j
public class HolidayPageHandler implements IPageHandler {
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;
	private Validator validator;

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
		pageData.setDate(LocalDate.parse(pageRequest.getData().get("date").asText()));
		validate(pageData);
		Holiday holiday = mapHolidayData(pageRequest, pageData);
		holiday = holidayService.create(holiday);
		mapHeaderData(pageRequest, pageDto, pageData, holiday);
		pageData.setLastUpdate(holiday.getOperationDateTime());
		pageData.setId(holiday.getHolidayId());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, HolidayPageData pageData, Holiday holiday) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Holiday mapHolidayData(PageRequestDTO pageRequest, HolidayPageData pageData) {
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday.setOperatorId(pageRequest.getData().get("operator").asText());
		holiday.setRecordInUse(RecordInUseType.Y);
		return holiday;
	}

	@SneakyThrows
	private PageDTO updateHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		validate(pageData);
		Holiday holiday = mapHolidayData(pageRequest, pageData);
		holiday = holidayService.update(holiday);
		mapHeaderData(pageRequest, pageDto, pageData, holiday);
		pageData.setLastUpdate(holiday.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday = holidayService.read(holiday);
		mapHeaderData(pageRequest, pageDto, pageData, holiday);
		mapPageData(pageData, holiday);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(HolidayPageData pageData, Holiday holiday) {
		pageData.setId(holiday.getHolidayId());
		pageData.setEditionId(holiday.getEditionId());
		pageData.setHoliday(holiday.getHoliday());
		pageData.setDate(holiday.getHolidayDate());
		pageData.setRecordInUse(holiday.getRecordInUse().toString());
		pageData.setOperator(holiday.getOperatorId());
		pageData.setLastUpdate(holiday.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeHoliday(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HolidayPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HolidayPageData.class);
		Holiday holiday = modelMapper.map(pageData, Holiday.class);
		holiday.setHolidayId(pageData.getId());
		holiday = holidayService.close(holiday);
		mapHeaderData(pageRequest, pageDto, pageData, holiday);
		mapPageData(pageData, holiday);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listHoliday(PageRequestDTO pageRequest) {
		Page<Holiday> editionList = holidayService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<HolidayPageData> list = mapHolidayData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
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
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				HolidayPageData holidayPageData = modelMapper.map(holiday, HolidayPageData.class);
				holidayPageData.setId(holiday.getHolidayId());
				holidayPageData.setOperator(holiday.getOperatorId());
				holidayPageData.setLastUpdate(holiday.getOperationDateTime());
				holidayPageDataList.add(holidayPageData);
			}
		}
		return holidayPageDataList;
	}

	private void validate(HolidayPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<HolidayPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
