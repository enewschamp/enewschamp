package com.enewschamp.app.admin.entitlement.handler;


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
import com.enewschamp.app.admin.entitlement.service.EntitlementService;
import com.enewschamp.app.admin.handler.ListPageData;
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

@Component("EntitlementPageHandler")
@Slf4j
public class EntitlementPageHandler implements IPageHandler {
	@Autowired
	private EntitlementService entitlementService;
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
			pageDto = createEntitlement(pageRequest);
			break;
		case "Update":
			pageDto = updateEntitlement(pageRequest);
			break;
		case "Read":
			pageDto = readEntitlement(pageRequest);
			break;
		case "Close":
			pageDto = closeEntitlement(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateEntitlement(pageRequest);
			break;
		case "List":
			pageDto = listEntitlement(pageRequest);
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
	private PageDTO createEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		validateData(pageData);
		Entitlement entitlement = mapEntitlementData(pageRequest, pageData);
		entitlement = entitlementService.create(entitlement);
		mapEntitlement(pageRequest, pageDto, entitlement);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);

	}

	private Entitlement mapEntitlementData(PageRequestDTO pageRequest, EntitlementPageData pageData) {
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement.setRecordInUse(RecordInUseType.Y);
		return entitlement;
	}

	@SneakyThrows
	private PageDTO updateEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		validateData(pageData);
		Entitlement entitlement = mapEntitlementData(pageRequest, pageData);
		entitlement = entitlementService.update(entitlement);
		mapEntitlement(pageRequest, pageDto, entitlement);
		return pageDto;
	}

	private void mapEntitlement(PageRequestDTO pageRequest, PageDTO pageDto, Entitlement entitlement) {
		EntitlementPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(entitlement);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement = entitlementService.read(entitlement);
		mapEntitlement(pageRequest, pageDto, entitlement);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement = entitlementService.reinstate(entitlement);
		mapEntitlement(pageRequest, pageDto, entitlement);
		return pageDto;
	}

	private EntitlementPageData mapPageData(Entitlement entitlement) {
		EntitlementPageData pageData = modelMapper.map(entitlement, EntitlementPageData.class);
		pageData.setLastUpdate(entitlement.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement = entitlementService.close(entitlement);
		mapEntitlement(pageRequest, pageDto, entitlement);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listEntitlement(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<Entitlement> entitlementList = entitlementService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<EntitlementPageData> list = mapEntitlementData(entitlementList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((entitlementList.getNumber() + 1) == entitlementList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<EntitlementPageData> mapEntitlementData(Page<Entitlement> page) {
		List<EntitlementPageData> entitlementPageDataList = new ArrayList<EntitlementPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Entitlement> pageDataList = page.getContent();
			for (Entitlement entitlement : pageDataList) {
				EntitlementPageData entitlementPageData = modelMapper.map(entitlement, EntitlementPageData.class);
				entitlementPageData.setLastUpdate(entitlement.getOperationDateTime());
				entitlementPageDataList.add(entitlementPageData);
			}
		}
		return entitlementPageDataList;
	}

	private void validateData(EntitlementPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<EntitlementPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}
