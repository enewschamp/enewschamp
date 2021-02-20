package com.enewschamp.app.admin.student.subscription.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StudentSubscriptionPageHandler")
public class StudentSubscriptionPageHandler implements IPageHandler {

	@Autowired
	private StudentSubscriptionService studentSubscriptionService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentSubscription(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentSubscription(pageRequest);
			break;
		case "Read":
			pageDto = readStudentSubscription(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentSubscription(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentSubscription(pageRequest);
			break;
		case "List":
			pageDto = listStudentSubscription(pageRequest);
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
	private PageDTO createStudentSubscription(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSubscriptionPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentSubscription studentSubscription = mapStudentSubscriptionData(pageRequest, pageData);
		studentSubscription = studentSubscriptionService.create(studentSubscription);
		mapStudentSubscription(pageRequest, pageDto, studentSubscription);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentSubscription(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSubscriptionPageData.class);
		validate(pageData,  this.getClass().getName());
		StudentSubscription studentSubscription = mapStudentSubscriptionData(pageRequest, pageData);
		studentSubscription = studentSubscriptionService.update(studentSubscription);
		mapStudentSubscription(pageRequest, pageDto, studentSubscription);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentSubscription(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSubscriptionPageData.class);
		StudentSubscription studentSubscription = modelMapper.map(pageData, StudentSubscription.class);
		studentSubscription = studentSubscriptionService.read(studentSubscription);
		mapStudentSubscription(pageRequest, pageDto, studentSubscription);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentSubscription(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSubscriptionPageData.class);
		StudentSubscription studentSubscription = modelMapper.map(pageData, StudentSubscription.class);
		studentSubscription = studentSubscriptionService.close(studentSubscription);
		mapStudentSubscription(pageRequest, pageDto, studentSubscription);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentSubscription(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentSubscriptionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSubscriptionPageData.class);
		StudentSubscription studentSubscription = modelMapper.map(pageData, StudentSubscription.class);
		studentSubscription = studentSubscriptionService.reinstate(studentSubscription);
		mapStudentSubscription(pageRequest, pageDto, studentSubscription);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentSubscription(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentSubscription> studentSubscriptionList = studentSubscriptionService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentSubscriptionPageData> list = mapStudentSubscriptionData(studentSubscriptionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentSubscriptionList.getNumber() + 1) == studentSubscriptionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapStudentSubscription(PageRequestDTO pageRequest, PageDTO pageDto,
			StudentSubscription studentSubscription) {
		StudentSubscriptionPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentSubscription);
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

	private StudentSubscription mapStudentSubscriptionData(PageRequestDTO pageRequest,
			StudentSubscriptionPageData pageData) {
		StudentSubscription studentSubscription = modelMapper.map(pageData, StudentSubscription.class);
		studentSubscription.setRecordInUse(RecordInUseType.Y);
		return studentSubscription;
	}

	private StudentSubscriptionPageData mapPageData(StudentSubscription studentSubscription) {
		StudentSubscriptionPageData pageData = modelMapper.map(studentSubscription,
				StudentSubscriptionPageData.class);
		pageData.setLastUpdate(studentSubscription.getOperationDateTime());
		return pageData;
	}

	public List<StudentSubscriptionPageData> mapStudentSubscriptionData(Page<StudentSubscription> page) {
		List<StudentSubscriptionPageData> studentSubscriptionPageDataList = new ArrayList<StudentSubscriptionPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentSubscription> pageDataList = page.getContent();
			for (StudentSubscription studentSubscription : pageDataList) {
				StudentSubscriptionPageData pageData = modelMapper.map(studentSubscription,
						StudentSubscriptionPageData.class);
				pageData.setLastUpdate(studentSubscription.getOperationDateTime());
				studentSubscriptionPageDataList.add(pageData);
			}
		}
		return studentSubscriptionPageDataList;
	}

}
