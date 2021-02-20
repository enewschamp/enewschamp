package com.enewschamp.app.admin.student.school.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StudentSchoolPageHandler")
@EnableTransactionManagement
public class StudentSchoolPageHandler implements IPageHandler {

	@Autowired
	private StudentSchoolService studentSchoolService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentSchool(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentSchool(pageRequest);
			break;
		case "Read":
			pageDto = readStudentSchool(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentSchool(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentSchool(pageRequest);
			break;
		case "List":
			pageDto = listStudentSchool(pageRequest);
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
	private PageDTO createStudentSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentSchool studentSchool = mapStudentSchoolData(pageRequest, pageData);
		studentSchool = studentSchoolService.create(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentSchool studentSchool = mapStudentSchoolData(pageRequest, pageData);
		studentSchool = studentSchoolService.update(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentSchoolService.read(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentSchoolService.close(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentSchool(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSchoolPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolPageData.class);
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool = studentSchoolService.reinstate(studentSchool);
		mapStudentSchool(pageRequest, pageDto, studentSchool);
		return pageDto;
	}

	@SneakyThrows
	public PageDTO listStudentSchool(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentSchool> studentSchoolList = studentSchoolService.list(searchRequest,
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

}
