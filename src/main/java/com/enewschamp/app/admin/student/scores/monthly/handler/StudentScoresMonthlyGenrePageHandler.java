package com.enewschamp.app.admin.student.scores.monthly.handler;

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
import com.enewschamp.app.admin.student.scores.monthly.repository.StudentScoresMonthlyGenre;
import com.enewschamp.app.admin.student.scores.monthly.service.StudentScoresMonthlyGenreService;
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

@Component("StudentScoresMonthlyGenrePageHandler")
@Slf4j
public class StudentScoresMonthlyGenrePageHandler implements IPageHandler {
	@Autowired
	private StudentScoresMonthlyGenreService studentScoresMonthlyService;
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
			pageDto = createStudentScoresMonthlyGenre(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentScoresMonthlyGenre(pageRequest);
			break;
		case "Read":
			pageDto = readStudentScoresMonthlyGenre(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentScoresMonthlyGenre(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentScoresMonthlyGenre(pageRequest);
			break;
		case "List":
			pageDto = listStudentScoresMonthlyGenre(pageRequest);
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
	private PageDTO createStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyGenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyGenrePageData.class);
		validate(pageData);
		StudentScoresMonthlyGenre studentScoreMonthly = mapStudentScoresMonthlyGenreData(pageRequest, pageData);
		studentScoreMonthly = studentScoresMonthlyService.create(studentScoreMonthly);
		mapStudentScoresMonthlyGenre(pageRequest, pageDto, studentScoreMonthly);
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

	private StudentScoresMonthlyGenre mapStudentScoresMonthlyGenreData(PageRequestDTO pageRequest, StudentScoresMonthlyGenrePageData pageData) {
		StudentScoresMonthlyGenre studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyGenre.class);
		studentScoresMonthly.setRecordInUse(RecordInUseType.Y);
		return studentScoresMonthly;
	}

	@SneakyThrows
	private PageDTO updateStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyGenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyGenrePageData.class);
		validate(pageData);
		StudentScoresMonthlyGenre studentScoresMonthly = mapStudentScoresMonthlyGenreData(pageRequest, pageData);
		studentScoresMonthly = studentScoresMonthlyService.update(studentScoresMonthly);
		mapStudentScoresMonthlyGenre(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyGenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyGenrePageData.class);
		StudentScoresMonthlyGenre studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyGenre.class);
		studentScoresMonthly = studentScoresMonthlyService.read(studentScoresMonthly);
		mapStudentScoresMonthlyGenre(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyGenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyGenrePageData.class);
		StudentScoresMonthlyGenre studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyGenre.class);
		studentScoresMonthly = studentScoresMonthlyService.close(studentScoresMonthly);
		mapStudentScoresMonthlyGenre(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresMonthlyGenrePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresMonthlyGenrePageData.class);
		StudentScoresMonthlyGenre studentScoresMonthly = modelMapper.map(pageData, StudentScoresMonthlyGenre.class);
		studentScoresMonthly = studentScoresMonthlyService.reinstate(studentScoresMonthly);
		mapStudentScoresMonthlyGenre(pageRequest, pageDto, studentScoresMonthly);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentScoresMonthlyGenre(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentScoresMonthlyGenre> studentScoresMonthlyList = studentScoresMonthlyService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentScoresMonthlyGenrePageData> list = mapStudentScoresMonthlyGenreData(studentScoresMonthlyList);
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

	private StudentScoresMonthlyGenrePageData mapPageData(StudentScoresMonthlyGenre studentScoresMonthly) {
		StudentScoresMonthlyGenrePageData pageData = modelMapper.map(studentScoresMonthly, StudentScoresMonthlyGenrePageData.class);
		pageData.setLastUpdate(studentScoresMonthly.getOperationDateTime());
		return pageData;
	}

	private void mapStudentScoresMonthlyGenre(PageRequestDTO pageRequest, PageDTO pageDto, StudentScoresMonthlyGenre studentScoresMonthly) {
		StudentScoresMonthlyGenrePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentScoresMonthly);
		pageDto.setData(pageData);
	}

	public List<StudentScoresMonthlyGenrePageData> mapStudentScoresMonthlyGenreData(Page<StudentScoresMonthlyGenre> page) {
		List<StudentScoresMonthlyGenrePageData> studentScoresMonthlyPageDataList = new ArrayList<StudentScoresMonthlyGenrePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentScoresMonthlyGenre> pageDataList = page.getContent();
			for (StudentScoresMonthlyGenre studentScoresMonthly : pageDataList) {
				StudentScoresMonthlyGenrePageData studentScoresMonthlyPageData = modelMapper.map(studentScoresMonthly, StudentScoresMonthlyGenrePageData.class);
				studentScoresMonthlyPageData.setLastUpdate(studentScoresMonthly.getOperationDateTime());
				studentScoresMonthlyPageDataList.add(studentScoresMonthlyPageData);
			}
		}
		return studentScoresMonthlyPageDataList;
	}

	private void validate(StudentScoresMonthlyGenrePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentScoresMonthlyGenrePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}