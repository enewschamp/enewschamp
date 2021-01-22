package com.enewschamp.app.admin.student.preference.handler;

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
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.service.StudentPreferencesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentPreferencesPageHandler")
@Slf4j
public class StudentPreferencesPageHandler implements IPageHandler {
	@Autowired
	private StudentPreferencesService StudentPreferencesService;
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
			pageDto = createStudentPreferences(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentPreferences(pageRequest);
			break;
		case "Read":
			pageDto = readStudentPreferences(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentPreferences(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInStudentPreferences(pageRequest);
			break;
		case "List":
			pageDto = listStudentPreferences(pageRequest);
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
	private PageDTO createStudentPreferences(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPreferencesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentPreferencesPageData.class);
		validateData(pageData);
		StudentPreferences studentPreferences = mapStudentPreferencesPageData(pageRequest, pageData);
		studentPreferences = StudentPreferencesService.create(studentPreferences);
		mapStudentPreferences(pageRequest, pageDto, studentPreferences);
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

	private StudentPreferences mapStudentPreferencesPageData(PageRequestDTO pageRequest, StudentPreferencesPageData pageData) {
		StudentPreferences studentPreferences = modelMapper.map(pageData, StudentPreferences.class);
		studentPreferences.setRecordInUse(RecordInUseType.Y);
		return studentPreferences;
	}

	@SneakyThrows
	private PageDTO updateStudentPreferences(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPreferencesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentPreferencesPageData.class);
		validateData(pageData);
		StudentPreferences studentPreferences = mapStudentPreferencesPageData(pageRequest, pageData);
		studentPreferences = StudentPreferencesService.update(studentPreferences);
		mapStudentPreferences(pageRequest, pageDto, studentPreferences);
		return pageDto;
	}

	private void mapStudentPreferences(PageRequestDTO pageRequest, PageDTO pageDto, StudentPreferences StudentPreferences) {
		StudentPreferencesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(StudentPreferences);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readStudentPreferences(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPreferencesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentPreferencesPageData.class);
		StudentPreferences studentPreferences = modelMapper.map(pageData, StudentPreferences.class);
		studentPreferences = StudentPreferencesService.read(studentPreferences);
		mapStudentPreferences(pageRequest, pageDto, studentPreferences);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInStudentPreferences(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPreferencesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentPreferencesPageData.class);
		StudentPreferences studentPreferences = modelMapper.map(pageData, StudentPreferences.class);
		studentPreferences = StudentPreferencesService.reInStudentPreferences(studentPreferences);
		mapStudentPreferences(pageRequest, pageDto, studentPreferences);
		return pageDto;
	}

	private StudentPreferencesPageData mapPageData(StudentPreferences StudentPreferences) {
		StudentPreferencesPageData pageData = modelMapper.map(StudentPreferences, StudentPreferencesPageData.class);
		pageData.setLastUpdate(StudentPreferences.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeStudentPreferences(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPreferencesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentPreferencesPageData.class);
		StudentPreferences studentPreferences = modelMapper.map(pageData, StudentPreferences.class);
		studentPreferences = StudentPreferencesService.close(studentPreferences);
		mapStudentPreferences(pageRequest, pageDto, studentPreferences);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentPreferences(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(
				pageRequest.getData().get(CommonConstants.FILTER).get(CommonConstants.COUNTRY_ID).asText());
		Page<StudentPreferences> studentPreferencesList = StudentPreferencesService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentPreferencesPageData> list = mapStudentPreferencesPageData(studentPreferencesList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentPreferencesList.getNumber() + 1) == studentPreferencesList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentPreferencesPageData> mapStudentPreferencesPageData(Page<StudentPreferences> page) {
		List<StudentPreferencesPageData> StudentPreferencesPageDataList = new ArrayList<StudentPreferencesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentPreferences> pageDataList = page.getContent();
			for (StudentPreferences studentPreferences : pageDataList) {
				StudentPreferencesPageData studentPreferencesPageData = modelMapper.map(studentPreferences, StudentPreferencesPageData.class);
				studentPreferencesPageData.setLastUpdate(studentPreferences.getOperationDateTime());
				StudentPreferencesPageDataList.add(studentPreferencesPageData);
			}
		}
		return StudentPreferencesPageDataList;
	}

	private void validateData(StudentPreferencesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentPreferencesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}