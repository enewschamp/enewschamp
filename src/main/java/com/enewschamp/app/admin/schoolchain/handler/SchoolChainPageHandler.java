package com.enewschamp.app.admin.schoolchain.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;
import com.enewschamp.app.admin.schoolchain.service.SchoolChainService;
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

@Component("SchoolChainPageHandler")
public class SchoolChainPageHandler implements IPageHandler {

	@Autowired
	private SchoolChainService schoolChainService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createSchoolChain(pageRequest);
			break;
		case "Update":
			pageDto = updateSchoolChain(pageRequest);
			break;
		case "Read":
			pageDto = readSchoolChain(pageRequest);
			break;
		case "Close":
			pageDto = closeSchoolChain(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInstateSchoolChain(pageRequest);
			break;
		case "List":
			pageDto = listSchoolChain(pageRequest);
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
	private PageDTO createSchoolChain(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolChainPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolChainPageData.class);
		validate(pageData,  this.getClass().getName());
		SchoolChain schoolChain = mapSchoolChainData(pageRequest, pageData);
		schoolChain = schoolChainService.create(schoolChain);
		mapSchoolChain(pageRequest, pageDto, schoolChain);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateSchoolChain(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolChainPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolChainPageData.class);
		validate(pageData,  this.getClass().getName());
		SchoolChain schoolChain = mapSchoolChainData(pageRequest, pageData);
		schoolChain = schoolChainService.update(schoolChain);
		mapSchoolChain(pageRequest, pageDto, schoolChain);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readSchoolChain(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolChainPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolChainPageData.class);
		SchoolChain schoolChain = modelMapper.map(pageData, SchoolChain.class);
		schoolChain = schoolChainService.read(schoolChain);
		mapSchoolChain(pageRequest, pageDto, schoolChain);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeSchoolChain(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolChainPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolChainPageData.class);
		SchoolChain schoolChain = modelMapper.map(pageData, SchoolChain.class);
		schoolChain = schoolChainService.close(schoolChain);
		mapSchoolChain(pageRequest, pageDto, schoolChain);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateSchoolChain(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		SchoolChainPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				SchoolChainPageData.class);
		SchoolChain schoolChain = modelMapper.map(pageData, SchoolChain.class);
		schoolChain = schoolChainService.reinstate(schoolChain);
		mapSchoolChain(pageRequest, pageDto, schoolChain);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listSchoolChain(PageRequestDTO pageRequest) {
		Page<SchoolChain> schoolChainList = schoolChainService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<SchoolChainPageData> list = mapSchoolChainData(schoolChainList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((schoolChainList.getNumber() + 1) == schoolChainList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapSchoolChain(PageRequestDTO pageRequest, PageDTO pageDto, SchoolChain schoolChain) {
		SchoolChainPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(schoolChain);
		pageDto.setData(pageData);
	}

	private SchoolChain mapSchoolChainData(PageRequestDTO pageRequest, SchoolChainPageData pageData) {
		SchoolChain schoolChain = modelMapper.map(pageData, SchoolChain.class);
		schoolChain.setRecordInUse(RecordInUseType.Y);
		return schoolChain;
	}

	private SchoolChainPageData mapPageData(SchoolChain schoolChain) {
		SchoolChainPageData pageData = modelMapper.map(schoolChain, SchoolChainPageData.class);
		pageData.setLastUpdate(schoolChain.getOperationDateTime());
		return pageData;
	}

	public List<SchoolChainPageData> mapSchoolChainData(Page<SchoolChain> page) {
		List<SchoolChainPageData> schoolChainPageDataList = new ArrayList<SchoolChainPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<SchoolChain> pageDataList = page.getContent();
			for (SchoolChain schoolChain : pageDataList) {
				SchoolChainPageData statePageData = modelMapper.map(schoolChain, SchoolChainPageData.class);
				statePageData.setLastUpdate(schoolChain.getOperationDateTime());
				schoolChainPageDataList.add(statePageData);
			}
		}
		return schoolChainPageDataList;
	}

}
