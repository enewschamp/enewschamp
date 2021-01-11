package com.enewschamp.app.admin.student.achievement.handler;

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
import com.enewschamp.app.admin.student.school.handler.StudentSchoolPageData;
import com.enewschamp.app.admin.student.school.handler.StudentSchoolPageHandler;
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
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.enewschamp.subscription.domain.service.StudentShareAchievementsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentShareAchievementsPageHandler")
@Slf4j
public class StudentShareAchievementsPageHandler implements IPageHandler {

	@Autowired
	private StudentShareAchievementsService studentShareAchievementsService;
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
			pageDto = createStudentAchievement(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentAchievement(pageRequest);
			break;
		case "Read":
			pageDto = readStudentAchievement(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentAchievement(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentAchievement(pageRequest);
			break;
		case "List":
			pageDto = listStudentAchievement(pageRequest);
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
	private PageDTO createStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		validateData(pageData);
		StudentSchool studentSchool = mapStudentSchoolData(pageRequest, pageData);
		studentSchool = studentShareAchievementsService.create(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		validateData(pageData);
		StudentSchool studentSchool = mapStudentSchoolData(pageRequest, pageData);
		studentSchool = studentShareAchievementsService.update(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentShareAchievementsService.read(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentShareAchievementsService.close(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentAchievement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentShareAchievementsService.reinstate(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentAchievement(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentSchool> studentSchoolList = studentShareAchievementsService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentSchoolPageData> list = mapStudentSchoolData(studentSchoolList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentSchoolList.getNumber() + 1) == studentSchoolList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapStudentSchool(PageRequestDTO pageRequest, PageDTO pageDto,
			StudentSchool studentSchool) {
		StudentSchoolPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentSchool);
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

	private StudentSchool mapStudentSchoolData(PageRequestDTO pageRequest,
			StudentSchoolPageData pageData) {
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool.setRecordInUse(RecordInUseType.Y);
		return studentSchool;
	}

	private StudentSchoolPageData mapPageData(StudentSchool studentSchool) {
		StudentSchoolPageData pageData = modelMapper.map(studentSchool,
				StudentSchoolPageData.class);
		pageData.setLastUpdate(studentSchool.getOperationDateTime());
		return pageData;
	}

	public List<StudentSchoolPageData> mapStudentSchoolData(Page<StudentSchool> page) {
		List<StudentSchoolPageData> studentSchoolPageDataList = new ArrayList<StudentSchoolPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentSchool> pageDataList = page.getContent();
			for (StudentSchool studentSchool : pageDataList) {
				StudentSchoolPageData pageData = modelMapper.map(studentSchool,
						StudentSchoolPageData.class);
				pageData.setLastUpdate(studentSchool.getOperationDateTime());
				studentSchoolPageDataList.add(pageData);
			}
		}
		return studentSchoolPageDataList;
	}

	private void validateData(StudentSchoolPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentSchoolPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}
