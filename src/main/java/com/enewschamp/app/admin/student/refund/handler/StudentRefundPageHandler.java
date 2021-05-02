package com.enewschamp.app.admin.student.refund.handler;

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
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.user.domain.entity.StudentRefund;
import com.enewschamp.user.domain.service.StudentRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StudentRefundPageHandler")
public class StudentRefundPageHandler implements IPageHandler {
	@Autowired
	private StudentRefundService refundService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentRefund(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentRefund(pageRequest);
			break;
		case "Read":
			pageDto = readStudentRefund(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentRefund(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentRefund(pageRequest);
			break;
		case "List":
			pageDto = listStudentRefund(pageRequest);
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
	private PageDTO createStudentRefund(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRefundPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRefundPageData.class);
		validate(pageData, this.getClass().getName());
		StudentRefund StudentRefund = mapStudentRefundData(pageRequest, pageData);
		StudentRefund = refundService.create(StudentRefund);
		mapStudentRefund(pageRequest, pageDto, StudentRefund);
		return pageDto;
	}

	@SneakyThrows
	private StudentRefund mapStudentRefundData(PageRequestDTO pageRequest, StudentRefundPageData pageData) {
		StudentRefund studentRefund = modelMapper.map(pageData, StudentRefund.class);
		studentRefund.setInitRefundApiRequest(stringToBlob(pageData.getInitRefundApiRequest()));
		studentRefund.setInitRefundApiResponse(stringToBlob(pageData.getInitRefundApiResponse()));
		studentRefund.setRefundStatusApiRequest(stringToBlob(pageData.getInitRefundApiRequest()));
		studentRefund.setRefundStatusApiResponse(stringToBlob(pageData.getInitRefundApiResponse()));
		studentRefund.setRecordInUse(RecordInUseType.Y);
		return studentRefund;
	}

	@SneakyThrows
	private PageDTO updateStudentRefund(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRefundPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRefundPageData.class);
		validate(pageData, this.getClass().getName());
		StudentRefund StudentRefund = mapStudentRefundData(pageRequest, pageData);
		StudentRefund = refundService.update(StudentRefund);
		mapStudentRefund(pageRequest, pageDto, StudentRefund);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentRefund(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRefundPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRefundPageData.class);
		StudentRefund StudentRefund = modelMapper.map(pageData, StudentRefund.class);
		StudentRefund = refundService.read(StudentRefund);
		mapStudentRefund(pageRequest, pageDto, StudentRefund);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentRefund(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRefundPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRefundPageData.class);
		StudentRefund StudentRefund = modelMapper.map(pageData, StudentRefund.class);
		StudentRefund = refundService.close(StudentRefund);
		mapStudentRefund(pageRequest, pageDto, StudentRefund);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentRefund(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRefundPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRefundPageData.class);
		StudentRefund StudentRefund = modelMapper.map(pageData, StudentRefund.class);
		StudentRefund = refundService.reinstate(StudentRefund);
		mapStudentRefund(pageRequest, pageDto, StudentRefund);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentRefund(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentRefund> StudentRefundList = refundService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentRefundPageData> list = mapStudentRefundData(StudentRefundList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((StudentRefundList.getNumber() + 1) == StudentRefundList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	@SneakyThrows
	private StudentRefundPageData mapPageData(StudentRefund studentRefund) {
		StudentRefundPageData pageData = modelMapper.map(studentRefund, StudentRefundPageData.class);
		pageData.setInitRefundApiRequest(blobToString(studentRefund.getInitRefundApiRequest()));
		pageData.setInitRefundApiResponse(blobToString(studentRefund.getInitRefundApiResponse()));
		pageData.setRefundStatusApiRequest(blobToString(studentRefund.getRefundStatusApiRequest()));
		pageData.setRefundStatusApiResponse(blobToString(studentRefund.getRefundStatusApiResponse()));
		pageData.setLastUpdate(studentRefund.getOperationDateTime());
		return pageData;
	}

	private void mapStudentRefund(PageRequestDTO pageRequest, PageDTO pageDto, StudentRefund StudentRefund) {
		StudentRefundPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(StudentRefund);
		pageDto.setData(pageData);
	}

	public List<StudentRefundPageData> mapStudentRefundData(Page<StudentRefund> page) {
		List<StudentRefundPageData> StudentRefundPageDataList = new ArrayList<StudentRefundPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentRefund> pageDataList = page.getContent();
			for (StudentRefund studentRefund : pageDataList) {
				StudentRefundPageData StudentRefundPageData = mapPageData(studentRefund);
				StudentRefundPageData.setLastUpdate(studentRefund.getOperationDateTime());
				StudentRefundPageDataList.add(StudentRefundPageData);
			}
		}
		return StudentRefundPageDataList;
	}

}
