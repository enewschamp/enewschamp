package com.enewschamp.app.admin.student.payment.failed.handler;

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
import com.enewschamp.subscription.domain.entity.StudentPaymentFailed;
import com.enewschamp.subscription.domain.service.StudentPaymentFailedService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component("StudentPaymentFailedPageHandler")
@RequiredArgsConstructor
public class StudentPaymentFailedPageHandler implements IPageHandler {
	private final StudentPaymentFailedService studentPaymentFailedService;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "List":
			pageDto = listStudentPaymentFailed(pageRequest);
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
	private PageDTO listStudentPaymentFailed(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentPaymentFailed> StudentPaymentFailedList = studentPaymentFailedService.listStudentPaymentFailed(
				searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentPaymentFailedPageData> list = mapStudentPaymentFailedData(StudentPaymentFailedList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((StudentPaymentFailedList.getNumber() + 1) == StudentPaymentFailedList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StudentPaymentFailedPageData> mapStudentPaymentFailedData(Page<StudentPaymentFailed> page) {
		List<StudentPaymentFailedPageData> userLoginPageDataList = new ArrayList<StudentPaymentFailedPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentPaymentFailed> pageDataList = page.getContent();
			for (StudentPaymentFailed StudentPaymentFailed : pageDataList) {
				StudentPaymentFailedPageData userLoginPageData = mapPageData(StudentPaymentFailed);
				userLoginPageData.setLastUpdate(StudentPaymentFailed.getOperationDateTime());
				userLoginPageDataList.add(userLoginPageData);
			}
		}
		return userLoginPageDataList;
	}

	@SneakyThrows
	private StudentPaymentFailedPageData mapPageData(StudentPaymentFailed studentPaymentFailed) {
		StudentPaymentFailedPageData pageData = modelMapper.map(studentPaymentFailed,
				StudentPaymentFailedPageData.class);
		pageData.setLastUpdate(studentPaymentFailed.getOperationDateTime());
		pageData.setTranStatusApiRequest(blobToString(studentPaymentFailed.getTranStatusApiRequest()));
		pageData.setTranStatusApiResponse(blobToString(studentPaymentFailed.getTranStatusApiResponse()));
		pageData.setInitTranApiRequest(blobToString(studentPaymentFailed.getInitTranApiRequest()));
		pageData.setInitTranApiResponse(blobToString(studentPaymentFailed.getInitTranApiResponse()));
		return pageData;
	}

}