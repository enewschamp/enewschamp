package com.enewschamp.app.admin.schoolpricing.handler;

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
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("SchoolPricingPageHandler")
@Slf4j
public class SchoolPricingPageHandler implements IPageHandler {

	@Autowired
	private SchoolPricingService schoolPricingService;
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
			pageDto = createSchoolPricing(pageRequest);
			break;
		case "Update":
			pageDto = updateSchoolPricing(pageRequest);
			break;
		case "Read":
			pageDto = readSchoolPricing(pageRequest);
			break;
		case "Close":
			pageDto = closeSchoolPricing(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateSchoolPricing(pageRequest);
			break;
		case "List":
			pageDto = listSchoolPricing(pageRequest);
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
	private PageDTO createSchoolPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolPricingPageData.class);
		validateData(pageData);
		SchoolPricing schoolPricing = mapSchoolPricingData(pageRequest, pageData);
		schoolPricing = schoolPricingService.create(schoolPricing);
		mapSchoolPricing(pageRequest, pageDto, schoolPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateSchoolPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolPricingPageData.class);
		validateData(pageData);
		SchoolPricing schoolPricing = mapSchoolPricingData(pageRequest, pageData);
		schoolPricing = schoolPricingService.update(schoolPricing);
		mapSchoolPricing(pageRequest, pageDto, schoolPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readSchoolPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolPricingPageData.class);
		SchoolPricing schoolPricing = modelMapper.map(pageData, SchoolPricing.class);
		schoolPricing = schoolPricingService.read(schoolPricing);
		mapSchoolPricing(pageRequest, pageDto, schoolPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeSchoolPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolPricingPageData.class);
		SchoolPricing schoolPricing = modelMapper.map(pageData, SchoolPricing.class);
		schoolPricing = schoolPricingService.close(schoolPricing);
		mapSchoolPricing(pageRequest, pageDto, schoolPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateSchoolPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolPricingPageData.class);
		SchoolPricing schoolPricing = modelMapper.map(pageData, SchoolPricing.class);
		schoolPricing = schoolPricingService.reInstate(schoolPricing);
		mapSchoolPricing(pageRequest, pageDto, schoolPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listSchoolPricing(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<SchoolPricing> schoolPricingList = schoolPricingService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<SchoolPricingPageData> list = mapSchoolPricingData(schoolPricingList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((schoolPricingList.getNumber() + 1) == schoolPricingList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapSchoolPricing(PageRequestDTO pageRequest, PageDTO pageDto,
			SchoolPricing schoolPricing) {
		SchoolPricingPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(schoolPricing);
		pageDto.setData(pageData);
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);
	}

	private SchoolPricing mapSchoolPricingData(PageRequestDTO pageRequest,
			SchoolPricingPageData pageData) {
		SchoolPricing schoolPricing = modelMapper.map(pageData, SchoolPricing.class);
		schoolPricing.setRecordInUse(RecordInUseType.Y);
		return schoolPricing;
	}

	private SchoolPricingPageData mapPageData(SchoolPricing schoolPricing) {
		SchoolPricingPageData pageData = modelMapper.map(schoolPricing,
				SchoolPricingPageData.class);
		pageData.setLastUpdate(schoolPricing.getOperationDateTime());
		return pageData;
	}

	public List<SchoolPricingPageData> mapSchoolPricingData(Page<SchoolPricing> page) {
		List<SchoolPricingPageData> schoolPricingPageDataList = new ArrayList<SchoolPricingPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<SchoolPricing> pageDataList = page.getContent();
			for (SchoolPricing schoolPricing : pageDataList) {
				SchoolPricingPageData pageData = modelMapper.map(schoolPricing,
						SchoolPricingPageData.class);
				pageData.setLastUpdate(schoolPricing.getOperationDateTime());
				schoolPricingPageDataList.add(pageData);
			}
		}
		return schoolPricingPageDataList;
	}

	private void validateData(SchoolPricingPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<SchoolPricingPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}