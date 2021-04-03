package com.enewschamp.app.admin.student.scores.daily.handler;

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
import com.enewschamp.app.admin.student.scores.daily.repository.StudentScoresDaily;
import com.enewschamp.app.admin.student.scores.daily.service.StudentScoresDailyService;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentScoresDailyPageHandler")
@Slf4j
public class StudentScoresDailyPageHandler implements IPageHandler {
	@Autowired
	private StudentScoresDailyService studentScoresDailyService;
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
			pageDto = createStudentScoresDaily(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentScoresDaily(pageRequest);
			break;
		case "Read":
			pageDto = readStudentScoresDaily(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentScoresDaily(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentScoresDaily(pageRequest);
			break;
		case "List":
			pageDto = listStudentScoresDaily(pageRequest);
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
	private PageDTO createStudentScoresDaily(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresDailyPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresDailyPageData.class);
		validate(pageData);
		StudentScoresDaily studentScoreDaily = mapStudentScoresDailyData(pageRequest, pageData);
		studentScoreDaily = studentScoresDailyService.create(studentScoreDaily);
		mapStudentScoresDaily(pageRequest, pageDto, studentScoreDaily);
		return pageDto;
	}

	private StudentScoresDaily mapStudentScoresDailyData(PageRequestDTO pageRequest, StudentScoresDailyPageData pageData) {
		StudentScoresDaily studentScoresDaily = modelMapper.map(pageData, StudentScoresDaily.class);
		studentScoresDaily.setRecordInUse(RecordInUseType.Y);
		return studentScoresDaily;
	}

	@SneakyThrows
	private PageDTO updateStudentScoresDaily(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresDailyPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresDailyPageData.class);
		validate(pageData);
		StudentScoresDaily studentScoresDaily = mapStudentScoresDailyData(pageRequest, pageData);
		studentScoresDaily = studentScoresDailyService.update(studentScoresDaily);
		mapStudentScoresDaily(pageRequest, pageDto, studentScoresDaily);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentScoresDaily(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresDailyPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresDailyPageData.class);
		StudentScoresDaily studentScoresDaily = modelMapper.map(pageData, StudentScoresDaily.class);
		studentScoresDaily = studentScoresDailyService.read(studentScoresDaily);
		mapStudentScoresDaily(pageRequest, pageDto, studentScoresDaily);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentScoresDaily(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresDailyPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresDailyPageData.class);
		StudentScoresDaily studentScoresDaily = modelMapper.map(pageData, StudentScoresDaily.class);
		studentScoresDaily = studentScoresDailyService.close(studentScoresDaily);
		mapStudentScoresDaily(pageRequest, pageDto, studentScoresDaily);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentScoresDaily(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentScoresDailyPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StudentScoresDailyPageData.class);
		StudentScoresDaily studentScoresDaily = modelMapper.map(pageData, StudentScoresDaily.class);
		studentScoresDaily = studentScoresDailyService.reinstate(studentScoresDaily);
		mapStudentScoresDaily(pageRequest, pageDto, studentScoresDaily);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentScoresDaily(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentScoresDaily> studentScoresDailyList = studentScoresDailyService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentScoresDailyPageData> list = mapStudentScoresDailyData(studentScoresDailyList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentScoresDailyList.getNumber() + 1) == studentScoresDailyList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private StudentScoresDailyPageData mapPageData(StudentScoresDaily studentScoresDaily) {
		StudentScoresDailyPageData pageData = modelMapper.map(studentScoresDaily, StudentScoresDailyPageData.class);
		pageData.setLastUpdate(studentScoresDaily.getOperationDateTime());
		return pageData;
	}

	private void mapStudentScoresDaily(PageRequestDTO pageRequest, PageDTO pageDto, StudentScoresDaily studentScoresDaily) {
		StudentScoresDailyPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentScoresDaily);
		pageDto.setData(pageData);
	}

	public List<StudentScoresDailyPageData> mapStudentScoresDailyData(Page<StudentScoresDaily> page) {
		List<StudentScoresDailyPageData> studentScoresDailyPageDataList = new ArrayList<StudentScoresDailyPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentScoresDaily> pageDataList = page.getContent();
			for (StudentScoresDaily studentScoresDaily : pageDataList) {
				StudentScoresDailyPageData studentScoresDailyPageData = modelMapper.map(studentScoresDaily, StudentScoresDailyPageData.class);
				studentScoresDailyPageData.setLastUpdate(studentScoresDaily.getOperationDateTime());
				studentScoresDailyPageDataList.add(studentScoresDailyPageData);
			}
		}
		return studentScoresDailyPageDataList;
	}

	private void validate(StudentScoresDailyPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentScoresDailyPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}