package com.enewschamp.app.admin.student.badges.handler;

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
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.badges.service.StudentBadgesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentBadgesPageHandler")
@Slf4j
public class StudentBadgesPageHandler implements IPageHandler {
	@Autowired
	private StudentBadgesService studentBadgesService;
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
			pageDto = createStudentBadge(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentBadge(pageRequest);
			break;
		case "Read":
			pageDto = readStudentBadge(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentBadge(pageRequest);
			break;
		case "ReinstudentBadges":
			pageDto = reInstudentBadgesStudentBadge(pageRequest);
			break;
		case "List":
			pageDto = listStudentBadge(pageRequest);
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
	private PageDTO createStudentBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentBadgesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentBadgesPageData.class);
		validateData(pageData);
		StudentBadges studentBadges = mapStudentBadgesData(pageRequest, pageData);
		studentBadges = studentBadgesService.create(studentBadges);
		mapStudentBadges(pageRequest, pageDto, studentBadges);
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

	private StudentBadges mapStudentBadgesData(PageRequestDTO pageRequest, StudentBadgesPageData pageData) {
		StudentBadges studentBadges = modelMapper.map(pageData, StudentBadges.class);
		studentBadges.setRecordInUse(RecordInUseType.Y);
		return studentBadges;
	}

	@SneakyThrows
	private PageDTO updateStudentBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentBadgesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentBadgesPageData.class);
		validateData(pageData);
		StudentBadges studentBadges = mapStudentBadgesData(pageRequest, pageData);
		studentBadges = studentBadgesService.update(studentBadges);
		mapStudentBadges(pageRequest, pageDto, studentBadges);
		return pageDto;
	}

	private void mapStudentBadges(PageRequestDTO pageRequest, PageDTO pageDto, StudentBadges studentBadges) {
		StudentBadgesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentBadges);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readStudentBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentBadgesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentBadgesPageData.class);
		StudentBadges studentBadges = modelMapper.map(pageData, StudentBadges.class);
		studentBadges = studentBadgesService.read(studentBadges);
		mapStudentBadges(pageRequest, pageDto, studentBadges);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstudentBadgesStudentBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentBadgesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentBadgesPageData.class);
		StudentBadges studentBadges = modelMapper.map(pageData, StudentBadges.class);
		studentBadges = studentBadgesService.reInstate(studentBadges);
		mapStudentBadges(pageRequest, pageDto, studentBadges);
		return pageDto;
	}

	private StudentBadgesPageData mapPageData(StudentBadges studentBadges) {
		StudentBadgesPageData pageData = modelMapper.map(studentBadges, StudentBadgesPageData.class);
		pageData.setLastUpdate(studentBadges.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeStudentBadge(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentBadgesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentBadgesPageData.class);
		StudentBadges studentBadges = modelMapper.map(pageData, StudentBadges.class);
		studentBadges = studentBadgesService.close(studentBadges);
		mapStudentBadges(pageRequest, pageDto, studentBadges);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentBadge(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(
				pageRequest.getData().get(CommonConstants.FILTER).get(CommonConstants.COUNTRY_ID).asText());
		Page<StudentBadges> studentBadgesList = studentBadgesService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentBadgesPageData> list = mapStudentBadgesData(studentBadgesList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentBadgesList.getNumber() + 1) == studentBadgesList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentBadgesPageData> mapStudentBadgesData(Page<StudentBadges> page) {
		List<StudentBadgesPageData> studentBadgesPageDataList = new ArrayList<StudentBadgesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentBadges> pageDataList = page.getContent();
			for (StudentBadges studentBadges : pageDataList) {
				StudentBadgesPageData studentBadgesPageData = modelMapper.map(studentBadges, StudentBadgesPageData.class);
				studentBadgesPageData.setLastUpdate(studentBadges.getOperationDateTime());
				studentBadgesPageDataList.add(studentBadgesPageData);
			}
		}
		return studentBadgesPageDataList;
	}

	private void validateData(StudentBadgesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentBadgesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}