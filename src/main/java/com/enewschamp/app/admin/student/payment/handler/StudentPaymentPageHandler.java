package com.enewschamp.app.admin.student.payment.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component("StudentPaymentPageHandler")
@RequiredArgsConstructor
public class StudentPaymentPageHandler implements IPageHandler {
	private final StudentPaymentService studentPaymentService;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentPayment(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentPayment(pageRequest);
			break;
		case "Read":
			pageDto = readStudentPayment(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentPayment(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentPayment(pageRequest);
			break;
		case "List":
			pageDto = listStudentPayment(pageRequest);
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
	private PageDTO createStudentPayment(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPaymentPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentPaymentPageData.class);
		validate(pageData, this.getClass().getName());
		StudentPayment StudentPayment = mapStudentPaymentData(pageRequest, pageData);
		StudentPayment = studentPaymentService.create(StudentPayment);
		mapStudentPayment(pageRequest, pageDto, StudentPayment);
		return pageDto;
	}

	@SneakyThrows
	private StudentPayment mapStudentPaymentData(PageRequestDTO pageRequest, StudentPaymentPageData pageData) {
		StudentPayment studentPayment = modelMapper.map(pageData, StudentPayment.class);
		studentPayment.setTranStatusApiRequest(stringToBlob(pageData.getTranStatusApiRequest()));
		studentPayment.setTranStatusApiResponse(stringToBlob(pageData.getTranStatusApiRequest()));
		studentPayment.setInitTranApiRequest(stringToBlob(pageData.getInitTranApiRequest()));
		studentPayment.setInitTranApiResponse(stringToBlob(pageData.getInitTranApiResponse()));
		studentPayment.setRecordInUse(RecordInUseType.Y);
		return studentPayment;
	}

	@SneakyThrows
	private PageDTO updateStudentPayment(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPaymentPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentPaymentPageData.class);
		validate(pageData, this.getClass().getName());
		StudentPayment StudentPayment = mapStudentPaymentData(pageRequest, pageData);
		StudentPayment = studentPaymentService.update(StudentPayment);
		mapStudentPayment(pageRequest, pageDto, StudentPayment);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentPayment(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPaymentPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentPaymentPageData.class);
		StudentPayment StudentPayment = modelMapper.map(pageData, StudentPayment.class);
		StudentPayment = studentPaymentService.read(StudentPayment);
		mapStudentPayment(pageRequest, pageDto, StudentPayment);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentPayment(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPaymentPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentPaymentPageData.class);
		StudentPayment StudentPayment = modelMapper.map(pageData, StudentPayment.class);
		StudentPayment = studentPaymentService.close(StudentPayment);
		mapStudentPayment(pageRequest, pageDto, StudentPayment);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentPayment(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentPaymentPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentPaymentPageData.class);
		StudentPayment StudentPayment = modelMapper.map(pageData, StudentPayment.class);
		StudentPayment = studentPaymentService.reinstate(StudentPayment);
		mapStudentPayment(pageRequest, pageDto, StudentPayment);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentPayment(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentPayment> studentPaymentList = studentPaymentService.listStudentPayment(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentPaymentPageData> list = mapStudentPaymentData(studentPaymentList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((studentPaymentList.getNumber() + 1) == studentPaymentList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentPaymentPageData> mapStudentPaymentData(Page<StudentPayment> page) {
		List<StudentPaymentPageData> userLoginPageDataList = new ArrayList<StudentPaymentPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentPayment> pageDataList = page.getContent();
			for (StudentPayment studentPayment : pageDataList) {
				StudentPaymentPageData userLoginPageData = mapPageData(studentPayment);
				userLoginPageData.setLastUpdate(studentPayment.getOperationDateTime());
				userLoginPageDataList.add(userLoginPageData);
			}
		}
		return userLoginPageDataList;
	}
	
	private void mapStudentPayment(PageRequestDTO pageRequest, PageDTO pageDto, StudentPayment StudentPayment) {
		StudentPaymentPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(StudentPayment);
		pageDto.setData(pageData);
	}
	
	@SneakyThrows
	private StudentPaymentPageData mapPageData(StudentPayment studentPayment) {
		StudentPaymentPageData pageData = modelMapper.map(studentPayment, StudentPaymentPageData.class);
		pageData.setLastUpdate(studentPayment.getOperationDateTime());
		pageData.setTranStatusApiRequest(blobToString(studentPayment.getTranStatusApiRequest()));
		pageData.setTranStatusApiResponse(blobToString(studentPayment.getTranStatusApiResponse()));
	    pageData.setInitTranApiRequest(blobToString(studentPayment.getInitTranApiRequest()));
	    pageData.setInitTranApiResponse(blobToString(studentPayment.getInitTranApiResponse()));
		return pageData;
	}

}