package com.enewschamp.app.admin.institutionstakeholder.handler;

import java.time.LocalDate;
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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.entitlement.repository.Entitlement;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.institutionstakeholder.entity.InstitutionStakeholder;
import com.enewschamp.app.admin.institutionstakeholder.service.InstitutionStakeholderService;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("InstitutionStakeholderPageHandler")
@Slf4j
public class InstitutionStakeholderPageHandler implements IPageHandler {

	@Autowired
	private InstitutionStakeholderService instStackHolderService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createInstitutionStakeholder(pageRequest);
			break;
		case "Update":
			pageDto = updateInstitutionStakeholder(pageRequest);
			break;
		case "Read":
			pageDto = readInstitutionStakeholder(pageRequest);
			break;
		case "Close":
			pageDto = closeInstitutionStakeholder(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateInstitutionStakeholder(pageRequest);
			break;
		case "List":
			pageDto = listInstitutionStakeholder(pageRequest);
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
	private PageDTO createInstitutionStakeholder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionStakeholderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionStakeholderPageData.class);
		validate(pageData,  this.getClass().getName());
		InstitutionStakeholder institutionStakeholder = mapInstitutionStakeholderData(pageRequest, pageData);
		institutionStakeholder = instStackHolderService.create(institutionStakeholder);
		mapInstitutionStakeholder(pageRequest, pageDto, institutionStakeholder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateInstitutionStakeholder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionStakeholderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionStakeholderPageData.class);
		validate(pageData,  this.getClass().getName());
		InstitutionStakeholder institutionStakeholder = mapInstitutionStakeholderData(pageRequest, pageData);
		institutionStakeholder = instStackHolderService.update(institutionStakeholder);
		mapInstitutionStakeholder(pageRequest, pageDto, institutionStakeholder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readInstitutionStakeholder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionStakeholderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionStakeholderPageData.class);
		InstitutionStakeholder institutionStakeholder = modelMapper.map(pageData, InstitutionStakeholder.class);
		institutionStakeholder = instStackHolderService.read(institutionStakeholder);
		mapInstitutionStakeholder(pageRequest, pageDto, institutionStakeholder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeInstitutionStakeholder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionStakeholderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionStakeholderPageData.class);
		InstitutionStakeholder institutionStakeholder = modelMapper.map(pageData, InstitutionStakeholder.class);
		institutionStakeholder = instStackHolderService.close(institutionStakeholder);
		mapInstitutionStakeholder(pageRequest, pageDto, institutionStakeholder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateInstitutionStakeholder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		InstitutionStakeholderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				InstitutionStakeholderPageData.class);
		InstitutionStakeholder institutionStakeholder = modelMapper.map(pageData, InstitutionStakeholder.class);
		institutionStakeholder = instStackHolderService.reInstate(institutionStakeholder);
		mapInstitutionStakeholder(pageRequest, pageDto, institutionStakeholder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listInstitutionStakeholder(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<InstitutionStakeholder> editionList = instStackHolderService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<InstitutionStakeholderPageData> list = mapInstitutionStakeholderData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapInstitutionStakeholder(PageRequestDTO pageRequest, PageDTO pageDto,
			InstitutionStakeholder institutionStakeholder) {
		InstitutionStakeholderPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(institutionStakeholder);
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

	private InstitutionStakeholder mapInstitutionStakeholderData(PageRequestDTO pageRequest,
			InstitutionStakeholderPageData pageData) {
		InstitutionStakeholder institutionStakeholder = modelMapper.map(pageData, InstitutionStakeholder.class);
		institutionStakeholder.setRecordInUse(RecordInUseType.Y);
		return institutionStakeholder;
	}

	private InstitutionStakeholderPageData mapPageData(InstitutionStakeholder institutionStakeholder) {
		InstitutionStakeholderPageData pageData = modelMapper.map(institutionStakeholder,
				InstitutionStakeholderPageData.class);
		pageData.setLastUpdate(institutionStakeholder.getOperationDateTime());
		return pageData;
	}

	public List<InstitutionStakeholderPageData> mapInstitutionStakeholderData(Page<InstitutionStakeholder> page) {
		List<InstitutionStakeholderPageData> institutionStakeholderPageDataList = new ArrayList<InstitutionStakeholderPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<InstitutionStakeholder> pageDataList = page.getContent();
			for (InstitutionStakeholder institutionStakeholder : pageDataList) {
				InstitutionStakeholderPageData statePageData = modelMapper.map(institutionStakeholder,
						InstitutionStakeholderPageData.class);
				statePageData.setLastUpdate(institutionStakeholder.getOperationDateTime());
				institutionStakeholderPageDataList.add(statePageData);
			}
		}
		return institutionStakeholderPageDataList;
	}

}
