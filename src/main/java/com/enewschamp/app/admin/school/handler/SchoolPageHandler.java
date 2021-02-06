package com.enewschamp.app.admin.school.handler;

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
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("SchoolPageHandler")
@Slf4j
public class SchoolPageHandler implements IPageHandler {

	@Autowired
	private SchoolService schoolService;
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
			pageDto = createSchool(pageRequest);
			break;
		case "Update":
			pageDto = updateSchool(pageRequest);
			break;
		case "Read":
			pageDto = readSchool(pageRequest);
			break;
		case "Close":
			pageDto = closeSchool(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInstateSchool(pageRequest);
			break;
		case "List":
			pageDto = listSchool(pageRequest);
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
	private PageDTO createSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), SchoolPageData.class);
		validateData(pageData);
		School school = mapSchoolData(pageRequest, pageData);
		school = schoolService.create(school);
		mapSchool(pageRequest, pageDto, school);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), SchoolPageData.class);
		validateData(pageData);
		School school = mapSchoolData(pageRequest, pageData);
		school = schoolService.update(school);
		mapSchool(pageRequest, pageDto, school);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), SchoolPageData.class);
		School school = modelMapper.map(pageData, School.class);
		school = schoolService.read(school);
		mapSchool(pageRequest, pageDto, school);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO closeSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), SchoolPageData.class);
		School school = modelMapper.map(pageData, School.class);
		school = schoolService.close(school);
		mapSchool(pageRequest, pageDto, school);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), SchoolPageData.class);
		School school = modelMapper.map(pageData, School.class);
		school = schoolService.reInstate(school);
		mapSchool(pageRequest, pageDto, school);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listSchool(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<School> editionList = schoolService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<SchoolPageData> list = mapSchoolData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapSchool(PageRequestDTO pageRequest, PageDTO pageDto, School school) {
		SchoolPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(school);
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

	private School mapSchoolData(PageRequestDTO pageRequest, SchoolPageData pageData) {
		School school = modelMapper.map(pageData, School.class);
		school.setRecordInUse(RecordInUseType.Y);
		return school;
	}

	private SchoolPageData mapPageData(School school) {
		SchoolPageData pageData = modelMapper.map(school, SchoolPageData.class);
		pageData.setLastUpdate(school.getOperationDateTime());
		return pageData;
	}


	public List<SchoolPageData> mapSchoolData(Page<School> page) {
		List<SchoolPageData> SchoolPageDataList = new ArrayList<SchoolPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<School> pageDataList = page.getContent();
			for (School school : pageDataList) {
				SchoolPageData statePageData = modelMapper.map(school, SchoolPageData.class);
				statePageData.setLastUpdate(school.getOperationDateTime());
				SchoolPageDataList.add(statePageData);
			}
		}
		return SchoolPageDataList;
	}

	private void validateData(SchoolPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<SchoolPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}
