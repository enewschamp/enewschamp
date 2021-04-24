package com.enewschamp.app.admin.errorcode.handler;

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
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.bulk.handler.BulkInsertResponsePageData;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("ErrorCodesPageHandler")
@Slf4j
@Transactional
public class ErrorCodesPageHandler implements IPageHandler {
	@Autowired
	private ErrorCodesService errorCodesService;
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
			pageDto = createErrorCodes(pageRequest);
			break;
		case "Update":
			pageDto = updateErrorCodes(pageRequest);
			break;
		case "Read":
			pageDto = readErrorCodes(pageRequest);
			break;
		case "Close":
			pageDto = closeErrorCodes(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateErrorCodes(pageRequest);
			break;
		case "List":
			pageDto = listErrorCodes(pageRequest);
			break;
		case "Insert":
			pageDto = insertAll(pageRequest);
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
	private PageDTO createErrorCodes(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageData.class);
		validate(pageData);
		ErrorCodes errorCodes = mapErrorCodesData(pageRequest, pageData);
		errorCodes = errorCodesService.create(errorCodes);
		mapErrorCodes(pageRequest, pageDto, errorCodes);
		return pageDto;
	}

	private ErrorCodes mapErrorCodesData(PageRequestDTO pageRequest, ErrorCodesPageData pageData) {
		ErrorCodes errorCodes = modelMapper.map(pageData, ErrorCodes.class);
		errorCodes.setRecordInUse(RecordInUseType.Y);
		return errorCodes;
	}

	@SneakyThrows
	private PageDTO updateErrorCodes(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageData.class);
		validate(pageData);
		ErrorCodes errorCodes = mapErrorCodesData(pageRequest, pageData);
		errorCodes = errorCodesService.update(errorCodes);
		mapErrorCodes(pageRequest, pageDto, errorCodes);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readErrorCodes(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageData.class);
		ErrorCodes errorCodes = modelMapper.map(pageData, ErrorCodes.class);
		errorCodes = errorCodesService.read(errorCodes);
		mapErrorCodes(pageRequest, pageDto, errorCodes);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeErrorCodes(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageData.class);
		ErrorCodes errorCodes = modelMapper.map(pageData, ErrorCodes.class);
		errorCodes = errorCodesService.close(errorCodes);
		mapErrorCodes(pageRequest, pageDto, errorCodes);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateErrorCodes(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageData.class);
		ErrorCodes errorCodes = modelMapper.map(pageData, ErrorCodes.class);
		errorCodes = errorCodesService.reinstate(errorCodes);
		mapErrorCodes(pageRequest, pageDto, errorCodes);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listErrorCodes(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<ErrorCodes> errorCodesList = errorCodesService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<ErrorCodesPageData> list = mapErrorCodesData(errorCodesList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((errorCodesList.getNumber() + 1) == errorCodesList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO insertAll(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		ErrorCodesPageRoot pageRoot = objectMapper.readValue(pageRequest.getData().toString(),
				ErrorCodesPageRoot.class);
		List<ErrorCodes> pageNavigators = mapErrorCodes(pageRequest, pageRoot.getErrorCodes());
		if (pageRoot.isCleanRequired())
			errorCodesService.clean();
		int totalRecords = errorCodesService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		mapHeaderData(pageRequest, pageDto);
		pageDto.setData(responseData);
		return pageDto;
	}

	private ErrorCodesPageData mapPageData(ErrorCodes errorCodes) {
		ErrorCodesPageData pageData = modelMapper.map(errorCodes, ErrorCodesPageData.class);
		pageData.setLastUpdate(errorCodes.getOperationDateTime());
		return pageData;
	}

	private void mapErrorCodes(PageRequestDTO pageRequest, PageDTO pageDto, ErrorCodes errorCodes) {
		ErrorCodesPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(errorCodes);
		pageDto.setData(pageData);
	}

	public List<ErrorCodesPageData> mapErrorCodesData(Page<ErrorCodes> page) {
		List<ErrorCodesPageData> errorCodesPageDataList = new ArrayList<ErrorCodesPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<ErrorCodes> pageDataList = page.getContent();
			for (ErrorCodes errorCodes : pageDataList) {
				ErrorCodesPageData errorCodesPageData = modelMapper.map(errorCodes, ErrorCodesPageData.class);
				errorCodesPageData.setLastUpdate(errorCodes.getOperationDateTime());
				errorCodesPageDataList.add(errorCodesPageData);
			}
		}
		return errorCodesPageDataList;
	}

	private void validate(ErrorCodesPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<ErrorCodesPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	private List<ErrorCodes> mapErrorCodes(PageRequestDTO pageRequest, List<ErrorCodesPageData> pageData) {
		List<ErrorCodes> errorCodes = pageData.stream().peek(element -> {
			element.setOperatorId(pageRequest.getHeader().getUserId());
			element.setRecordInUse(RecordInUseType.Y);
		}).map(errorcode -> modelMapper.map(errorcode, ErrorCodes.class)).collect(Collectors.toList());
		return errorCodes;
	}

}