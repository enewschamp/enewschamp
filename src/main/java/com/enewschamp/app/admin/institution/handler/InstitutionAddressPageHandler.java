package com.enewschamp.app.admin.institution.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.institution.entity.InstitutionAddress;
import com.enewschamp.app.admin.institution.service.InstitutionAddressService;
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

@Component("InstitutionAddressPageHandler")
public class InstitutionAddressPageHandler implements IPageHandler {

	@Autowired
	private InstitutionAddressService institutionAddressService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createInstitutionAddress(pageRequest);
			break;
		case "Update":
			pageDto = updateInstitutionAddress(pageRequest);
			break;
		case "Read":
			pageDto = readInstitutionAddress(pageRequest);
			break;
		case "Close":
			pageDto = closeInstitutionAddress(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateInstitutionAddress(pageRequest);
			break;
		case "List":
			pageDto = listInstitutionAddress(pageRequest);
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
	private PageDTO createInstitutionAddress(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionAddressPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionAddressPageData.class);
		validate(pageData, this.getClass().getName());
		InstitutionAddress institutionAddress = mapInstitutionAddressData(pageRequest, pageData);
		institutionAddress = institutionAddressService.create(institutionAddress);
		mapInstitutionAddress(pageRequest, pageDto, institutionAddress);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateInstitutionAddress(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionAddressPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionAddressPageData.class);
		validate(pageData, this.getClass().getName());
		InstitutionAddress institutionAddress = mapInstitutionAddressData(pageRequest, pageData);
		institutionAddress = institutionAddressService.update(institutionAddress);
		mapInstitutionAddress(pageRequest, pageDto, institutionAddress);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readInstitutionAddress(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionAddressPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionAddressPageData.class);
		InstitutionAddress institutionAddress = modelMapper.map(pageData, InstitutionAddress.class);
		institutionAddress = institutionAddressService.read(institutionAddress);
		mapInstitutionAddress(pageRequest, pageDto, institutionAddress);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeInstitutionAddress(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionAddressPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionAddressPageData.class);
		InstitutionAddress institutionAddress = modelMapper.map(pageData, InstitutionAddress.class);
		institutionAddress = institutionAddressService.close(institutionAddress);
		mapInstitutionAddress(pageRequest, pageDto, institutionAddress);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateInstitutionAddress(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionAddressPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionAddressPageData.class);
		InstitutionAddress institutionAddress = modelMapper.map(pageData, InstitutionAddress.class);
		institutionAddress = institutionAddressService.reinstate(institutionAddress);
		mapInstitutionAddress(pageRequest, pageDto, institutionAddress);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listInstitutionAddress(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<InstitutionAddress> editionList = institutionAddressService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<InstitutionAddressPageData> list = mapInstitutionAddressData(editionList);
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

	private void mapInstitutionAddress(PageRequestDTO pageRequest, PageDTO pageDto,
			InstitutionAddress institutionAddress) {
		InstitutionAddressPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(institutionAddress);
		pageDto.setData(pageData);
	}

	private InstitutionAddress mapInstitutionAddressData(PageRequestDTO pageRequest,
			InstitutionAddressPageData pageData) {
		InstitutionAddress institutionAddress = modelMapper.map(pageData, InstitutionAddress.class);
		institutionAddress.setRecordInUse(RecordInUseType.Y);
		return institutionAddress;
	}

	private InstitutionAddressPageData mapPageData(InstitutionAddress institutionAddress) {
		InstitutionAddressPageData pageData = modelMapper.map(institutionAddress, InstitutionAddressPageData.class);
		pageData.setLastUpdate(institutionAddress.getOperationDateTime());
		return pageData;
	}

	public List<InstitutionAddressPageData> mapInstitutionAddressData(Page<InstitutionAddress> page) {
		List<InstitutionAddressPageData> InstitutionAddressPageDataList = new ArrayList<InstitutionAddressPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<InstitutionAddress> pageDataList = page.getContent();
			for (InstitutionAddress institutionAddress : pageDataList) {
				InstitutionAddressPageData institutePageData = modelMapper.map(institutionAddress,
						InstitutionAddressPageData.class);
				institutePageData.setLastUpdate(institutionAddress.getOperationDateTime());
				InstitutionAddressPageDataList.add(institutePageData);
			}
		}
		return InstitutionAddressPageDataList;
	}

}