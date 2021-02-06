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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.promotion.repository.Promotion;
import com.enewschamp.app.admin.promotion.service.PromotionService;
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
		case "Reinstate":
			pageDto = reinstatePromotion(pageRequest);
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
        mapPromotion(pageRequest, pageDto, promotion);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updatePromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		validate(pageData);
		Promotion promotion = mapPromotionData(pageRequest, pageData);
		promotion = promotionService.update(promotion);
		mapPromotion(pageRequest, pageDto, promotion);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readPromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion = promotionService.read(promotion);
        mapPromotion(pageRequest, pageDto, promotion);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closePromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion = promotionService.close(promotion);
        mapPromotion(pageRequest, pageDto, promotion);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO reinstatePromotion(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PromotionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), PromotionPageData.class);
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion = promotionService.reinstate(promotion);
        mapPromotion(pageRequest, pageDto, promotion);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPromotion(PageRequestDTO pageRequest) {
		Page<Promotion> promotionList = promotionService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PromotionPageData> list = mapPromotionData(promotionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((promotionList.getNumber() + 1) == promotionList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
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
				PromotionPageData promotionPageData = modelMapper.map(promotion, PromotionPageData.class);
				promotionPageData.setLastUpdate(promotion.getOperationDateTime());
				promotionPageDataList.add(promotionPageData);
			}
		}
		return promotionPageDataList;
	}
	
	private void mapPromotion(PageRequestDTO pageRequest, PageDTO pageDto, Promotion promotion) {
		PromotionPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(promotion);
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

	private Promotion mapPromotionData(PageRequestDTO pageRequest, PromotionPageData pageData) {
		Promotion promotion = modelMapper.map(pageData, Promotion.class);
		promotion.setRecordInUse(RecordInUseType.Y);
		return promotion;
	}

	private PromotionPageData mapPageData(Promotion promotion) {
		PromotionPageData pageData = modelMapper.map(promotion, PromotionPageData.class);
		pageData.setLastUpdate(promotion.getOperationDateTime());
		return pageData;
	}

	private void validate(PromotionPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PromotionPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
