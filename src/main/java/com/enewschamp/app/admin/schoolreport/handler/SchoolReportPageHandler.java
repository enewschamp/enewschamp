package com.enewschamp.app.admin.schoolreport.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.schoolreport.entity.SchoolReport;
import com.enewschamp.app.admin.schoolreport.service.SchoolReportService;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("SchoolReportPageHandler")
public class SchoolReportPageHandler implements IPageHandler {

	@Autowired
	private SchoolReportService schoolReportService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createSchoolReport(pageRequest);
			break;
		case "Update":
			pageDto = updateSchoolReport(pageRequest);
			break;
		case "Read":
			pageDto = readSchoolReport(pageRequest);
			break;
		case "Close":
			pageDto = closeSchoolReport(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateSchoolReport(pageRequest);
			break;
		case "List":
			pageDto = listSchoolReport(pageRequest);
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
	private PageDTO createSchoolReport(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolReportPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolReportPageData.class);
		validate(pageData, this.getClass().getName());
		SchoolReport schoolReport = mapSchoolReportData(pageRequest, pageData);
		schoolReport = schoolReportService.create(schoolReport);
		mapSchoolReport(pageRequest, pageDto, schoolReport);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateSchoolReport(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolReportPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolReportPageData.class);
		validate(pageData, this.getClass().getName());
		SchoolReport schoolReport = mapSchoolReportData(pageRequest, pageData);
		schoolReport = schoolReportService.update(schoolReport);
		mapSchoolReport(pageRequest, pageDto, schoolReport);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readSchoolReport(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolReportPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolReportPageData.class);
		SchoolReport schoolReport = modelMapper.map(pageData, SchoolReport.class);
		schoolReport = schoolReportService.read(schoolReport);
		mapSchoolReport(pageRequest, pageDto, schoolReport);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeSchoolReport(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolReportPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolReportPageData.class);
		SchoolReport schoolReport = modelMapper.map(pageData, SchoolReport.class);
		schoolReport = schoolReportService.close(schoolReport);
		mapSchoolReport(pageRequest, pageDto, schoolReport);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateSchoolReport(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolReportPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolReportPageData.class);
		SchoolReport schoolReport = modelMapper.map(pageData, SchoolReport.class);
		schoolReport = schoolReportService.reInstate(schoolReport);
		mapSchoolReport(pageRequest, pageDto, schoolReport);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listSchoolReport(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<SchoolReport> schoolReportList = schoolReportService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<SchoolReportPageData> list = mapSchoolReportData(schoolReportList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((schoolReportList.getNumber() + 1) == schoolReportList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapSchoolReport(PageRequestDTO pageRequest, PageDTO pageDto, SchoolReport schoolReport) {
		SchoolReportPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(schoolReport);
		pageDto.setData(pageData);
	}

	private SchoolReport mapSchoolReportData(PageRequestDTO pageRequest, SchoolReportPageData pageData) {
		SchoolReport schoolReport = modelMapper.map(pageData, SchoolReport.class);
		schoolReport.setRecordInUse(RecordInUseType.Y);
		return schoolReport;
	}

	private SchoolReportPageData mapPageData(SchoolReport schoolReport) {
		SchoolReportPageData pageData = modelMapper.map(schoolReport, SchoolReportPageData.class);
		pageData.setLastUpdate(schoolReport.getOperationDateTime());
		return pageData;
	}

	public List<SchoolReportPageData> mapSchoolReportData(Page<SchoolReport> page) {
		List<SchoolReportPageData> schoolReportPageDataList = new ArrayList<SchoolReportPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<SchoolReport> pageDataList = page.getContent();
			for (SchoolReport schoolReport : pageDataList) {
				SchoolReportPageData pageData = modelMapper.map(schoolReport, SchoolReportPageData.class);
				pageData.setLastUpdate(schoolReport.getOperationDateTime());
				schoolReportPageDataList.add(pageData);
			}
		}
		return schoolReportPageDataList;
	}

}