package com.enewschamp.app.admin.pricing.handler;

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
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
import com.enewschamp.subscription.pricing.service.IndividualPricingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("IndividualPricingPageHandler")
@Slf4j
public class IndividualPricingPageHandler implements IPageHandler {
	@Autowired
	private IndividualPricingService individualPricingService;
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
			pageDto = createIndividualPricing(pageRequest);
			break;
		case "Update":
			pageDto = updateIndividualPricing(pageRequest);
			break;
		case "Read":
			pageDto = readIndividualPricing(pageRequest);
			break;
		case "Close":
			pageDto = closeIndividualPricing(pageRequest);
			break;
		case "List":
			pageDto = listIndividualPricing(pageRequest);
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
	private PageDTO createIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), IndividualPricingPageData.class);
		validate(pageData);
		IndividualPricing individualPricing = mapPricingData(pageRequest, pageData);
		individualPricing = individualPricingService.create(individualPricing);
		mapHeaderData(pageRequest, pageDto, pageData, individualPricing);
		pageData.setLastUpdate(individualPricing.getOperationDateTime());
		pageData.setId(individualPricing.getIndividualPricingId());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, IndividualPricingPageData pageData, IndividualPricing individualPricing) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private IndividualPricing mapPricingData(PageRequestDTO pageRequest, IndividualPricingPageData pageData) {
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing.setOperatorId(pageRequest.getData().get("operator").asText());
		individualPricing.setRecordInUse(RecordInUseType.Y);
		return individualPricing;
	}

	@SneakyThrows
	private PageDTO updateIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), IndividualPricingPageData.class);
		validate(pageData);
		IndividualPricing individualPricing = mapPricingData(pageRequest, pageData);
		individualPricing = individualPricingService.update(individualPricing);
		mapHeaderData(pageRequest, pageDto, pageData, individualPricing);
		pageData.setLastUpdate(individualPricing.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), IndividualPricingPageData.class);
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing = individualPricingService.read(individualPricing);
		mapHeaderData(pageRequest, pageDto, pageData, individualPricing);
		mapPageData(pageData, individualPricing);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(IndividualPricingPageData pageData, IndividualPricing individualPricing) {
		pageData.setId(individualPricing.getIndividualPricingId());
		pageData.setEditionId(individualPricing.getEditionId());
		pageData.setEffectiveDate(individualPricing.getEffectiveDate());
		pageData.setPricingDetails(individualPricing.getPricingDetails());
		pageData.setRecordInUse(individualPricing.getRecordInUse().toString());
		pageData.setOperator(individualPricing.getOperatorId());
		pageData.setLastUpdate(individualPricing.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), IndividualPricingPageData.class);
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing.setIndividualPricingId(pageData.getId());
		individualPricing = individualPricingService.close(individualPricing);
		mapHeaderData(pageRequest, pageDto, pageData, individualPricing);
		mapPageData(pageData, individualPricing);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listIndividualPricing(PageRequestDTO pageRequest) {
		Page<IndividualPricing> pricingList = individualPricingService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<IndividualPricingPageData> list = mapPricingData(pricingList);
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

	public List<IndividualPricingPageData> mapPricingData(Page<IndividualPricing> page) {
		List<IndividualPricingPageData> holidayPageDataList = new ArrayList<IndividualPricingPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<IndividualPricing> pageDataList = page.getContent();
			for (IndividualPricing individualPricing : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				IndividualPricingPageData holidayPageData = modelMapper.map(individualPricing, IndividualPricingPageData.class);
				holidayPageData.setId(individualPricing.getIndividualPricingId());
				holidayPageData.setOperator(individualPricing.getOperatorId());
				holidayPageData.setLastUpdate(individualPricing.getOperationDateTime());
				holidayPageDataList.add(holidayPageData);
			}
		}
		return holidayPageDataList;
	}

	private void validate(IndividualPricingPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<IndividualPricingPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
