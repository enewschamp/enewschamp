package com.enewschamp.app.admin.student.control.handler;

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

import com.enewschamp.app.admin.handler.ListPageData;
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
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentControlPageHandler")
@Slf4j
public class StudentControlPageHandler implements IPageHandler {
	@Autowired
	private StudentControlService studentControlService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Update":
			pageDto = updateStudentControl(pageRequest);
			break;
		case "List":
			pageDto = listStudentControl(pageRequest);
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
	private PageDTO updateStudentControl(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentControlPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentControlPageData.class);
		validate(pageData);
		StudentControl studentControl = mapStudentControlData(pageRequest, pageData);
		studentControl = studentControlService.update(studentControl);
		mapStudentControl(pageRequest, pageDto, studentControl);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentControl(PageRequestDTO pageRequest) {
		Page<StudentControl> studentControlList = studentControlService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentControlPageData> list = mapStudentControlData(studentControlList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((studentControlList.getNumber() + 1) == studentControlList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentControlPageData> mapStudentControlData(Page<StudentControl> page) {
		List<StudentControlPageData> studentControlPageDataList = new ArrayList<StudentControlPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentControl> pageDataList = page.getContent();
			for (StudentControl studentControl : pageDataList) {
				StudentControlPageData studentControlPageData = modelMapper.map(studentControl,
						StudentControlPageData.class);
				studentControlPageData.setLastUpdate(studentControl.getOperationDateTime());
				studentControlPageDataList.add(studentControlPageData);
			}
		}
		return studentControlPageDataList;
	}

	private void mapStudentControl(PageRequestDTO pageRequest, PageDTO pageDto, StudentControl studentControl) {
		StudentControlPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentControl);
		pageDto.setData(pageData);
	}

	private StudentControl mapStudentControlData(PageRequestDTO pageRequest, StudentControlPageData pageData) {
		StudentControl studentControl = modelMapper.map(pageData, StudentControl.class);
		studentControl.setRecordInUse(RecordInUseType.Y);
		return studentControl;
	}

	private StudentControlPageData mapPageData(StudentControl studentControl) {
		StudentControlPageData pageData = modelMapper.map(studentControl, StudentControlPageData.class);
		pageData.setLastUpdate(studentControl.getOperationDateTime());
		return pageData;
	}

	private void validate(StudentControlPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentControlPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
