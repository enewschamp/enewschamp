package com.enewschamp.app.admin.student.activity.handler;

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
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.service.StudentActivityService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StudentActivityPageHandler")
public class StudentActivityPageHandler implements IPageHandler {
	@Autowired
	private StudentActivityService studentPaymentService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "List":
			pageDto = listStudentActivity(pageRequest);
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
	private PageDTO listStudentActivity(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentActivity> studentActivityList = studentPaymentService.listStudentActivity(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentActivityPageData> list = mapStudentActivityData(studentActivityList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentActivityList.getNumber() + 1) == studentActivityList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentActivityPageData> mapStudentActivityData(Page<StudentActivity> page) {
		List<StudentActivityPageData> userLoginPageDataList = new ArrayList<StudentActivityPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentActivity> pageDataList = page.getContent();
			for (StudentActivity userLogin : pageDataList) {
				StudentActivityPageData userLoginPageData = modelMapper.map(userLogin, StudentActivityPageData.class);
				userLoginPageData.setLastUpdate(userLogin.getOperationDateTime());
				userLoginPageDataList.add(userLoginPageData);
			}
		}
		return userLoginPageDataList;
	}
}