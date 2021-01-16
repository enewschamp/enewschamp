package com.enewschamp.app.admin.student.scores.monthly.total.handler;

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
import com.enewschamp.app.admin.student.scores.monthly.total.repository.StudentScoresMonthlyTotal;
import com.enewschamp.app.admin.student.scores.monthly.total.service.StudentScoresMonthlyTotalService;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentScoresMonthlyTotalPageHandler")
@Slf4j
public class StudentScoresMonthlyTotalPageHandler implements IPageHandler {
	@Autowired
	private StudentScoresMonthlyTotalService studentScoresMonthlyService;
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
			pageDto = createStudentScoresMonthlyTotal(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentScoresMonthlyTotal(pageRequest);
			break;
		case "Read":
			pageDto = readStudentScoresMonthlyTotal(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentScoresMonthlyTotal(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentScoresMonthlyTotal(pageRequest);
			break;
		case "List":
			pageDto = listStudentScoresMonthlyTotal(pageRequest);
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
	private PageDTO createStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyTotalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyTotalPageData.class);
		validate(pageData);
		StudentScoresMonthlyTotal studentScoreMonthly = mapStudentScoresMonthlyTotalData(pageRequest, pageData);
		studentScoreMonthly = studentScoresMonthlyService.create(studentScoreMonthly);
		mapStudentScoresMonthlyTotal(pageRequest, pageDto, studentScoreMonthly);
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

	private StudentScoresMonthlyTotal mapStudentScoresMonthlyTotalData(PageRequestDTO pageRequest, StudentScoresMonthlyTotalPageData pageData) {
		StudentScoresMonthlyTotal studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyTotal.class);
		studentScoresMonthly.setRecordInUse(RecordInUseType.Y);
		return studentScoresMonthly;
	}

	@SneakyThrows
	private PageDTO updateStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyTotalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyTotalPageData.class);
		validate(pageData);
		StudentScoresMonthlyTotal studentScoresMonthly = mapStudentScoresMonthlyTotalData(pageRequest, pageData);
		studentScoresMonthly = studentScoresMonthlyService.update(studentScoresMonthly);
		mapStudentScoresMonthlyTotal(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyTotalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyTotalPageData.class);
		StudentScoresMonthlyTotal studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyTotal.class);
		studentScoresMonthly = studentScoresMonthlyService.read(studentScoresMonthly);
		mapStudentScoresMonthlyTotal(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyTotalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyTotalPageData.class);
		StudentScoresMonthlyTotal studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyTotal.class);
		studentScoresMonthly = studentScoresMonthlyService.close(studentScoresMonthly);
		mapStudentScoresMonthlyTotal(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyTotalPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyTotalPageData.class);
		StudentScoresMonthlyTotal studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyTotal.class);
		studentScoresMonthly = studentScoresMonthlyService.reinstate(studentScoresMonthly);
		mapStudentScoresMonthlyTotal(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentScoresMonthlyTotal(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentScoresMonthlyTotal> studentScoresMonthlyList = studentScoresMonthlyService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentScoresMonthlyTotalPageData> list = mapStudentScoresMonthlyTotalData(studentScoresMonthlyList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentScoresMonthlyList.getNumber() + 1) == studentScoresMonthlyList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private StudentScoresMonthlyTotalPageData mapPageData(StudentScoresMonthlyTotal studentScoresMonthly) {
		StudentScoresMonthlyTotalPageData pageData = modelMapper.map(studentScoresMonthly, StudentScoresMonthlyTotalPageData.class);
		pageData.setLastUpdate(studentScoresMonthly.getOperationDateTime());
		return pageData;
	}

	private void mapStudentScoresMonthlyTotal(PageRequestDTO pageRequest, PageDTO pageDto, StudentScoresMonthlyTotal studentScoresMonthly) {
		StudentScoresMonthlyTotalPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentScoresMonthly);
		pageDto.setData(pageData);
	}

	public List<StudentScoresMonthlyTotalPageData> mapStudentScoresMonthlyTotalData(Page<StudentScoresMonthlyTotal> page) {
		List<StudentScoresMonthlyTotalPageData> studentScoresMonthlyPageDataList = new ArrayList<StudentScoresMonthlyTotalPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentScoresMonthlyTotal> pageDataList = page.getContent();
			for (StudentScoresMonthlyTotal studentScoresMonthly : pageDataList) {
				StudentScoresMonthlyTotalPageData studentScoresMonthlyPageData = modelMapper.map(studentScoresMonthly, StudentScoresMonthlyTotalPageData.class);
				studentScoresMonthlyPageData.setLastUpdate(studentScoresMonthly.getOperationDateTime());
				studentScoresMonthlyPageDataList.add(studentScoresMonthlyPageData);
			}
		}
		return studentScoresMonthlyPageDataList;
	}

	private void validate(StudentScoresMonthlyTotalPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentScoresMonthlyTotalPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}