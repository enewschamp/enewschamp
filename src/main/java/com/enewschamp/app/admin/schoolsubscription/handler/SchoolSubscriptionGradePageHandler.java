package com.enewschamp.app.admin.schoolsubscription.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.schoolsubscription.entity.SchoolSubscriptionGrade;
import com.enewschamp.app.admin.schoolsubscription.service.SchoolSubscriptionGradeService;
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

@Component("SchoolSubscriptionGradePageHandler")
public class SchoolSubscriptionGradePageHandler implements IPageHandler {

	@Autowired
	private SchoolSubscriptionGradeService schoolSubscriptionGradeService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createSchoolSubscriptionGrade(pageRequest);
			break;
		case "Update":
			pageDto = updateSchoolSubscriptionGrade(pageRequest);
			break;
		case "Read":
			pageDto = readSchoolSubscriptionGrade(pageRequest);
			break;
		case "Close":
			pageDto = closeSchoolSubscriptionGrade(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateSchoolSubscriptionGrade(pageRequest);
			break;
		case "List":
			pageDto = listSchoolSubscriptionGrade(pageRequest);
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
	private PageDTO createSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolSubscriptionGradePageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolSubscriptionGradePageData.class);
		validate(pageData,  this.getClass().getName());
		SchoolSubscriptionGrade schoolSubscriptionGrade = mapSchoolSubscriptionGradeData(pageRequest, pageData);
		schoolSubscriptionGrade = schoolSubscriptionGradeService.create(schoolSubscriptionGrade);
		mapSchoolSubscriptionGrade(pageRequest, pageDto, schoolSubscriptionGrade);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolSubscriptionGradePageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolSubscriptionGradePageData.class);
		validate(pageData,  this.getClass().getName());
		SchoolSubscriptionGrade schoolSubscriptionGrade = mapSchoolSubscriptionGradeData(pageRequest, pageData);
		schoolSubscriptionGrade = schoolSubscriptionGradeService.update(schoolSubscriptionGrade);
		mapSchoolSubscriptionGrade(pageRequest, pageDto, schoolSubscriptionGrade);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolSubscriptionGradePageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolSubscriptionGradePageData.class);
		SchoolSubscriptionGrade schoolSubscriptionGrade = modelMapper.map(pageData, SchoolSubscriptionGrade.class);
		schoolSubscriptionGrade = schoolSubscriptionGradeService.read(schoolSubscriptionGrade);
		mapSchoolSubscriptionGrade(pageRequest, pageDto, schoolSubscriptionGrade);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolSubscriptionGradePageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolSubscriptionGradePageData.class);
		SchoolSubscriptionGrade schoolSubscriptionGrade = modelMapper.map(pageData, SchoolSubscriptionGrade.class);
		schoolSubscriptionGrade = schoolSubscriptionGradeService.close(schoolSubscriptionGrade);
		mapSchoolSubscriptionGrade(pageRequest, pageDto, schoolSubscriptionGrade);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolSubscriptionGradePageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolSubscriptionGradePageData.class);
		SchoolSubscriptionGrade schoolSubscriptionGrade = modelMapper.map(pageData, SchoolSubscriptionGrade.class);
		schoolSubscriptionGrade = schoolSubscriptionGradeService.reInstate(schoolSubscriptionGrade);
		mapSchoolSubscriptionGrade(pageRequest, pageDto, schoolSubscriptionGrade);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listSchoolSubscriptionGrade(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<SchoolSubscriptionGrade> editionList = schoolSubscriptionGradeService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<SchoolSubscriptionGradePageData> list = mapSchoolSubscriptionGradeData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapSchoolSubscriptionGrade(PageRequestDTO pageRequest, PageDTO pageDto,
			SchoolSubscriptionGrade schoolSubscriptionGrade) {
		SchoolSubscriptionGradePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(schoolSubscriptionGrade);
		pageDto.setData(pageData);
	}

	private SchoolSubscriptionGrade mapSchoolSubscriptionGradeData(PageRequestDTO pageRequest,
			SchoolSubscriptionGradePageData pageData) {
		SchoolSubscriptionGrade schoolSubscriptionGrade = modelMapper.map(pageData, SchoolSubscriptionGrade.class);
		schoolSubscriptionGrade.setRecordInUse(RecordInUseType.Y);
		return schoolSubscriptionGrade;
	}

	private SchoolSubscriptionGradePageData mapPageData(SchoolSubscriptionGrade schoolSubscriptionGrade) {
		SchoolSubscriptionGradePageData pageData = modelMapper.map(schoolSubscriptionGrade,
				SchoolSubscriptionGradePageData.class);
		pageData.setLastUpdate(schoolSubscriptionGrade.getOperationDateTime());
		return pageData;
	}

	public List<SchoolSubscriptionGradePageData> mapSchoolSubscriptionGradeData(Page<SchoolSubscriptionGrade> page) {
		List<SchoolSubscriptionGradePageData> schoolSubscriptionGradePageDataList = new ArrayList<SchoolSubscriptionGradePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<SchoolSubscriptionGrade> pageDataList = page.getContent();
			for (SchoolSubscriptionGrade schoolSubscriptionGrade : pageDataList) {
				SchoolSubscriptionGradePageData pageData = modelMapper.map(schoolSubscriptionGrade,
						SchoolSubscriptionGradePageData.class);
				pageData.setLastUpdate(schoolSubscriptionGrade.getOperationDateTime());
				schoolSubscriptionGradePageDataList.add(pageData);
			}
		}
		return schoolSubscriptionGradePageDataList;
	}

}
