package com.enewschamp.app.admin.stakeholder.handler;

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
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.stakeholder.entity.StakeHolder;
import com.enewschamp.app.admin.stakeholder.service.StakeHolderService;
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

@Component("StakeHolderPageHandler")
@Slf4j
public class StakeHolderPageHandler implements IPageHandler {

	@Autowired
	private StakeHolderService stackHolderService;
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
			pageDto = createStakeHolder(pageRequest);
			break;
		case "Update":
			pageDto = updateStakeHolder(pageRequest);
			break;
		case "Read":
			pageDto = readStakeHolder(pageRequest);
			break;
		case "Close":
			pageDto = closeStakeHolder(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStakeHolder(pageRequest);
			break;
		case "List":
			pageDto = listStakeHolder(pageRequest);
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
	private PageDTO createStakeHolder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StakeHolderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StakeHolderPageData.class);
		validateData(pageData);
		StakeHolder stakeHolder = mapStakeHolderData(pageRequest, pageData);
		stakeHolder = stackHolderService.create(stakeHolder);
		mapStakeHolder(pageRequest, pageDto, stakeHolder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStakeHolder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StakeHolderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StakeHolderPageData.class);
		validateData(pageData);
		StakeHolder stakeHolder = mapStakeHolderData(pageRequest, pageData);
		stakeHolder = stackHolderService.update(stakeHolder);
		mapStakeHolder(pageRequest, pageDto, stakeHolder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStakeHolder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StakeHolderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StakeHolderPageData.class);
		StakeHolder stakeHolder = modelMapper.map(pageData, StakeHolder.class);
		stakeHolder = stackHolderService.read(stakeHolder);
		mapStakeHolder(pageRequest, pageDto, stakeHolder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStakeHolder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StakeHolderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StakeHolderPageData.class);
		StakeHolder stakeHolder = modelMapper.map(pageData, StakeHolder.class);
		stakeHolder = stackHolderService.close(stakeHolder);
		mapStakeHolder(pageRequest, pageDto, stakeHolder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStakeHolder(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StakeHolderPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StakeHolderPageData.class);
		StakeHolder stakeHolder = modelMapper.map(pageData, StakeHolder.class);
		stakeHolder = stackHolderService.reInstate(stakeHolder);
		mapStakeHolder(pageRequest, pageDto, stakeHolder);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStakeHolder(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StakeHolder> editionList = stackHolderService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StakeHolderPageData> list = mapStakeHolderData(editionList);
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

	private void mapStakeHolder(PageRequestDTO pageRequest, PageDTO pageDto, StakeHolder stakeHolder) {
		StakeHolderPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(stakeHolder);
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

	private StakeHolder mapStakeHolderData(PageRequestDTO pageRequest, StakeHolderPageData pageData) {
		StakeHolder stakeHolder = modelMapper.map(pageData, StakeHolder.class);
		stakeHolder.setRecordInUse(RecordInUseType.Y);
		return stakeHolder;
	}

	private StakeHolderPageData mapPageData(StakeHolder stakeHolder) {
		StakeHolderPageData pageData = modelMapper.map(stakeHolder, StakeHolderPageData.class);
		pageData.setLastUpdate(stakeHolder.getOperationDateTime());
		return pageData;
	}

	public List<StakeHolderPageData> mapStakeHolderData(Page<StakeHolder> page) {
		List<StakeHolderPageData> stakeHolderPageDataList = new ArrayList<StakeHolderPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StakeHolder> pageDataList = page.getContent();
			for (StakeHolder stakeHolder : pageDataList) {
				StakeHolderPageData statePageData = modelMapper.map(stakeHolder, StakeHolderPageData.class);
				statePageData.setLastUpdate(stakeHolder.getOperationDateTime());
				stakeHolderPageDataList.add(statePageData);
			}
		}
		return stakeHolderPageDataList;
	}

	private void validateData(StakeHolderPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StakeHolderPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}
}