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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.entitlement.repository.Entitlement;
import com.enewschamp.app.admin.entitlement.service.EntitlementService;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
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
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;
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
       // pageData.setUserId(pageDto);
		validate(pageData);
		Entitlement entitlement = mapEntitlementData(pageRequest, pageData);
		entitlement = entitlementService.create(entitlement);
		mapHeaderData(pageRequest, pageDto, pageData, entitlement);
		pageData.setLastUpdate(entitlement.getOperationDateTime());
		pageData.setId(entitlement.getEntitlementId());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, EntitlementPageData pageData, Entitlement entitlement) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Entitlement mapEntitlementData(PageRequestDTO pageRequest, EntitlementPageData pageData) {
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement.setOperatorId(pageRequest.getData().get("operator").asText());
		entitlement.setRecordInUse(RecordInUseType.Y);
		return entitlement;
	}

	@SneakyThrows
	private PageDTO updateEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		validate(pageData);
		Entitlement entitlement = mapEntitlementData(pageRequest, pageData);
		entitlement = entitlementService.update(entitlement);
		mapHeaderData(pageRequest, pageDto, pageData, entitlement);
		pageData.setLastUpdate(entitlement.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement = entitlementService.read(entitlement);
		mapHeaderData(pageRequest, pageDto, pageData, entitlement);
		mapPageData(pageData, entitlement);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(EntitlementPageData pageData, Entitlement entitlement) {
		pageData.setId(entitlement.getEntitlementId());
		pageData.setUserId(entitlement.getUserId());
		pageData.setPageName(entitlement.getPageName());
		pageData.setRecordInUse(entitlement.getRecordInUse().toString());
		pageData.setOperator(entitlement.getOperatorId());
		pageData.setLastUpdate(entitlement.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeEntitlement(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EntitlementPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EntitlementPageData.class);
		Entitlement entitlement = modelMapper.map(pageData, Entitlement.class);
		entitlement.setEntitlementId(pageData.getId());
		entitlement = entitlementService.close(entitlement);
		mapHeaderData(pageRequest, pageDto, pageData, entitlement);
		mapPageData(pageData, entitlement);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listEntitlement(PageRequestDTO pageRequest) {
		Page<Entitlement> pricingList = entitlementService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<EntitlementPageData> list = mapEntitlementData(pricingList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((pricingList.getNumber() + 1) == pricingList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
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
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				EntitlementPageData entitlementPageData = modelMapper.map(entitlement, EntitlementPageData.class);
				entitlementPageData.setId(entitlement.getEntitlementId());
				entitlementPageData.setOperator(entitlement.getOperatorId());
				entitlementPageData.setLastUpdate(entitlement.getOperationDateTime());
				entitlementPageDataList.add(entitlementPageData);
			}
		}
		return entitlementPageDataList;
	}

	private void validate(EntitlementPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<EntitlementPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
