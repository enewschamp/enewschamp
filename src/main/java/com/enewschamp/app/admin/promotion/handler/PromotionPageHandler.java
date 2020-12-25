package com.enewschamp.app.admin.promotion.handler;

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
import com.enewschamp.app.admin.promotion.repository.Promotion;
import com.enewschamp.app.admin.promotion.service.PromotionService;
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

@Component("PromotionPageHandler")
@Slf4j
public class PromotionPageHandler implements IPageHandler {
	@Autowired
	private PromotionService promotionService;
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
			pageDto = createPromotion(pageRequest);
			break;
		case "Update":
			pageDto = updatePromotion(pageRequest);
			break;
		case "Read":
			pageDto = readPromotion(pageRequest);
			break;
		case "Close":
			pageDto = closePromotion(pageRequest);
			break;
		case "List":
			pageDto = listPromotion(pageRequest);
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
	private PageDTO createPromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		validate(pageData);
		Promotion promotion = mapPromotionData(pageRequest, pageData);
		promotion = promotionService.create(promotion);
		mapHeaderData(pageRequest, pageDto, pageData, promotion);
		pageData.setLastUpdate(promotion.getOperationDateTime());
		pageData.setId(promotion.getPromotionId());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, PromotionPageData pageData, Promotion promotion) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private Promotion mapPromotionData(PageRequestDTO pageRequest, PromotionPageData pageData) {
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion.setOperatorId(pageRequest.getData().get("operator").asText());
		promotion.setRecordInUse(RecordInUseType.Y);
		return promotion;
	}

	@SneakyThrows
	private PageDTO updatePromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		validate(pageData);
		Promotion promotion = mapPromotionData(pageRequest, pageData);
		promotion = promotionService.update(promotion);
		mapHeaderData(pageRequest, pageDto, pageData, promotion);
		pageData.setLastUpdate(promotion.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readPromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion = promotionService.read(promotion);
		mapHeaderData(pageRequest, pageDto, pageData, promotion);
		mapPageData(pageData, promotion);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(PromotionPageData pageData, Promotion promotion) {
		pageData.setId(promotion.getPromotionId());
		pageData.setEditionId(promotion.getEditionId());
		pageData.setCountryId(promotion.getCountryId());
		pageData.setCouponCode(promotion.getCouponCode());
		pageData.setStateId(promotion.getStateId());
		pageData.setCityId(promotion.getCityId());
		pageData.setDateFrom(promotion.getDateFrom());
		pageData.setDateTo(promotion.getDateTo());
		pageData.setPromotionDetails(promotion.getPromotionDetails());
		pageData.setDescription(promotion.getDescription());
		pageData.setRecordInUse(promotion.getRecordInUse().toString());
		pageData.setOperator(promotion.getOperatorId());
		pageData.setLastUpdate(promotion.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closePromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion.setPromotionId(pageData.getId());
		promotion = promotionService.close(promotion);
		mapHeaderData(pageRequest, pageDto, pageData, promotion);
		mapPageData(pageData, promotion);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPromotion(PageRequestDTO pageRequest) {
		Page<Promotion> promotionList = promotionService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<PromotionPageData> list = mapPromotionData(promotionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((promotionList.getNumber() + 1) == promotionList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<PromotionPageData> mapPromotionData(Page<Promotion> page) {
		List<PromotionPageData> promotionPageDataList = new ArrayList<PromotionPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Promotion> pageDataList = page.getContent();
			for (Promotion promotion : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				PromotionPageData holidayPageData = modelMapper.map(promotion, PromotionPageData.class);
				holidayPageData.setId(promotion.getPromotionId());
				holidayPageData.setOperator(promotion.getOperatorId());
				holidayPageData.setLastUpdate(promotion.getOperationDateTime());
				promotionPageDataList.add(holidayPageData);
			}
		}
		return promotionPageDataList;
	}

	private void validate(PromotionPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PromotionPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
