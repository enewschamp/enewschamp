package com.enewschamp.app.admin.student.details.handler;

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
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentDetailsPageHandler")
@Slf4j
public class StudentDetailsPageHandler implements IPageHandler {

	@Autowired
	private StudentDetailsService studentDetailsService;
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
			pageDto = createStudentDetails(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentDetails(pageRequest);
			break;
		case "Read":
			pageDto = readStudentDetails(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentDetails(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentDetails(pageRequest);
			break;
		case "List":
			pageDto = listStudentDetails(pageRequest);
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
	private PageDTO createStudentDetails(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentDetailsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentDetailsPageData.class);
		validateData(pageData);
		StudentDetails studentDetails = mapStudentDetailsData(pageRequest, pageData);
		studentDetails = studentDetailsService.create(studentDetails);
		mapStudentDetails(pageRequest, pageDto, studentDetails);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentDetails(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentDetailsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentDetailsPageData.class);
		validateData(pageData);
		StudentDetails studentDetails = mapStudentDetailsData(pageRequest, pageData);
		studentDetails = studentDetailsService.update(studentDetails);
		mapStudentDetails(pageRequest, pageDto, studentDetails);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentDetails(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentDetailsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentDetailsPageData.class);
		StudentDetails studentDetails = modelMapper.map(pageData, StudentDetails.class);
		studentDetails = studentDetailsService.read(studentDetails);
		mapStudentDetails(pageRequest, pageDto, studentDetails);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentDetails(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentDetailsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentDetailsPageData.class);
		StudentDetails studentDetails = modelMapper.map(pageData, StudentDetails.class);
		studentDetails = studentDetailsService.close(studentDetails);
		mapStudentDetails(pageRequest, pageDto, studentDetails);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentDetails(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentDetailsPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentDetailsPageData.class);
		StudentDetails studentDetails = modelMapper.map(pageData, StudentDetails.class);
		studentDetails = studentDetailsService.reinstate(studentDetails);
		mapStudentDetails(pageRequest, pageDto, studentDetails);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentDetails(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentDetails> studentDetailsList = studentDetailsService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentDetailsPageData> list = mapStudentDetailsData(studentDetailsList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentDetailsList.getNumber() + 1) == studentDetailsList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapStudentDetails(PageRequestDTO pageRequest, PageDTO pageDto,
			StudentDetails studentDetails) {
		StudentDetailsPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentDetails);
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

	private StudentDetails mapStudentDetailsData(PageRequestDTO pageRequest,
			StudentDetailsPageData pageData) {
		StudentDetails studentDetails = modelMapper.map(pageData, StudentDetails.class);
		studentDetails.setRecordInUse(RecordInUseType.Y);
		return studentDetails;
	}

	private StudentDetailsPageData mapPageData(StudentDetails studentDetails) {
		StudentDetailsPageData pageData = modelMapper.map(studentDetails,
				StudentDetailsPageData.class);
		pageData.setLastUpdate(studentDetails.getOperationDateTime());
		return pageData;
	}

	public List<StudentDetailsPageData> mapStudentDetailsData(Page<StudentDetails> page) {
		List<StudentDetailsPageData> studentDetailsPageDataList = new ArrayList<StudentDetailsPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentDetails> pageDataList = page.getContent();
			for (StudentDetails studentDetails : pageDataList) {
				StudentDetailsPageData pageData = modelMapper.map(studentDetails,
						StudentDetailsPageData.class);
				pageData.setLastUpdate(studentDetails.getOperationDateTime());
				studentDetailsPageDataList.add(pageData);
			}
		}
		return studentDetailsPageDataList;
	}

	private void validateData(StudentDetailsPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentDetailsPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}