package com.enewschamp.app.admin.pricing.handler;

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

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
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
		case "Reinstate":
			pageDto = reinstateIndividualPricing(pageRequest);
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
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				IndividualPricingPageData.class);
		validate(pageData);
		IndividualPricing individualPricing = mapPricingData(pageRequest, pageData);
		individualPricing = individualPricingService.create(individualPricing);
		mapPricing(pageRequest, pageDto, individualPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				IndividualPricingPageData.class);
		validate(pageData);
		IndividualPricing individualPricing = mapPricingData(pageRequest, pageData);
		individualPricing = individualPricingService.update(individualPricing);
		mapPricing(pageRequest, pageDto, individualPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				IndividualPricingPageData.class);
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing = individualPricingService.read(individualPricing);
		mapPricing(pageRequest, pageDto, individualPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				IndividualPricingPageData.class);
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing = individualPricingService.close(individualPricing);
		mapPricing(pageRequest, pageDto, individualPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateIndividualPricing(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		IndividualPricingPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				IndividualPricingPageData.class);
		IndividualPricing individualPricing = modelMapper.map(pageData, IndividualPricing.class);
		individualPricing = individualPricingService.reinstate(individualPricing);
		mapPricing(pageRequest, pageDto, individualPricing);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listIndividualPricing(PageRequestDTO pageRequest) {
		Page<IndividualPricing> pricingList = individualPricingService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<IndividualPricingPageData> list = mapPricingData(pricingList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((pricingList.getNumber() + 1) == pricingList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
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
				IndividualPricingPageData holidayPageData = modelMapper.map(individualPricing,
						IndividualPricingPageData.class);
				holidayPageData.setLastUpdate(individualPricing.getOperationDateTime());
				holidayPageDataList.add(holidayPageData);
			}
		}
		return holidayPageDataList;
	}

	private void mapPricing(PageRequestDTO pageRequest, PageDTO pageDto, IndividualPricing pricing) {
		IndividualPricingPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(pricing);
		pageDto.setData(pageData);
	}

	private IndividualPricing mapPricingData(PageRequestDTO pageRequest, IndividualPricingPageData pageData) {
		IndividualPricing pricing = modelMapper.map(pageData, IndividualPricing.class);
		pricing.setRecordInUse(RecordInUseType.Y);
		return pricing;
	}

	private IndividualPricingPageData mapPageData(IndividualPricing pricing) {
		IndividualPricingPageData pageData = modelMapper.map(pricing, IndividualPricingPageData.class);
		pageData.setLastUpdate(pricing.getOperationDateTime());
		return pageData;
	}

	private void validate(IndividualPricingPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<IndividualPricingPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
