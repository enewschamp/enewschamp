package com.enewschamp.app.admin.properties.frontend.handler;

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
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.common.domain.service.PropertiesFrontendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("PropertiesFrontendPageHandler")
@Slf4j
@Transactional
public class PropertiesFrontendPageHandler implements IPageHandler {
	@Autowired
	private PropertiesFrontendService propertiesFrontendService;
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
			pageDto = createPropertiesFrontend(pageRequest);
			break;
		case "Update":
			pageDto = updatePropertiesFrontend(pageRequest);
			break;
		case "Read":
			pageDto = readPropertiesFrontend(pageRequest);
			break;
		case "Close":
			pageDto = closePropertiesFrontend(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstatePropertiesFrontend(pageRequest);
			break;
		case "List":
			pageDto = listPropertiesFrontend(pageRequest);
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
	private PageDTO createPropertiesFrontend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesFrontendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesFrontendPageData.class);
		validate(pageData);
		PropertiesFrontend propertiesFrontend = mapPropertiesFrontendData(pageRequest, pageData);
		propertiesFrontend = propertiesFrontendService.create(propertiesFrontend);
		mapPropertiesFrontend(pageRequest, pageDto, propertiesFrontend);
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

	private PropertiesFrontend mapPropertiesFrontendData(PageRequestDTO pageRequest,
			PropertiesFrontendPageData pageData) {
		PropertiesFrontend propertiesFrontend = modelMapper.map(pageData, PropertiesFrontend.class);
		propertiesFrontend.setRecordInUse(RecordInUseType.Y);
		return propertiesFrontend;
	}

	@SneakyThrows
	private PageDTO updatePropertiesFrontend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesFrontendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesFrontendPageData.class);
		validate(pageData);
		PropertiesFrontend propertiesFrontend = mapPropertiesFrontendData(pageRequest, pageData);
		propertiesFrontend = propertiesFrontendService.update(propertiesFrontend);
		mapPropertiesFrontend(pageRequest, pageDto, propertiesFrontend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readPropertiesFrontend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesFrontendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesFrontendPageData.class);
		PropertiesFrontend propertiesFrontend = modelMapper.map(pageData, PropertiesFrontend.class);
		propertiesFrontend = propertiesFrontendService.read(propertiesFrontend);
		mapPropertiesFrontend(pageRequest, pageDto, propertiesFrontend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closePropertiesFrontend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesFrontendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesFrontendPageData.class);
		PropertiesFrontend propertiesFrontend = modelMapper.map(pageData, PropertiesFrontend.class);
		propertiesFrontend = propertiesFrontendService.close(propertiesFrontend);
		mapPropertiesFrontend(pageRequest, pageDto, propertiesFrontend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstatePropertiesFrontend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesFrontendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesFrontendPageData.class);
		PropertiesFrontend propertiesFrontend = modelMapper.map(pageData, PropertiesFrontend.class);
		propertiesFrontend = propertiesFrontendService.reinstate(propertiesFrontend);
		mapPropertiesFrontend(pageRequest, pageDto, propertiesFrontend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPropertiesFrontend(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<PropertiesFrontend> propertiesFrontendList = propertiesFrontendService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PropertiesFrontendPageData> list = mapPropertiesFrontendData(propertiesFrontendList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((propertiesFrontendList.getNumber() + 1) == propertiesFrontendList.getTotalPages()) {
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
		List<PropertiesFrontendPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<PropertiesFrontendPageData>>() {
				});
		List<PropertiesFrontend> pageNavigators = mapPropertiesFrontends(pageRequest, pageData);
		int totalRecords = propertiesFrontendService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.setData(responseData);
		return pageDto;
	}

	private PropertiesFrontendPageData mapPageData(PropertiesFrontend propertiesFrontend) {
		PropertiesFrontendPageData pageData = modelMapper.map(propertiesFrontend, PropertiesFrontendPageData.class);
		pageData.setLastUpdate(propertiesFrontend.getOperationDateTime());
		return pageData;
	}

	private void mapPropertiesFrontend(PageRequestDTO pageRequest, PageDTO pageDto,
			PropertiesFrontend propertiesFrontend) {
		PropertiesFrontendPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(propertiesFrontend);
		pageDto.setData(pageData);
	}

	public List<PropertiesFrontendPageData> mapPropertiesFrontendData(Page<PropertiesFrontend> page) {
		List<PropertiesFrontendPageData> propertiesFrontendPageDataList = new ArrayList<PropertiesFrontendPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<PropertiesFrontend> pageDataList = page.getContent();
			for (PropertiesFrontend propertiesFrontend : pageDataList) {
				PropertiesFrontendPageData propertiesFrontendPageData = modelMapper.map(propertiesFrontend,
						PropertiesFrontendPageData.class);
				propertiesFrontendPageData.setLastUpdate(propertiesFrontend.getOperationDateTime());
				propertiesFrontendPageDataList.add(propertiesFrontendPageData);
			}
		}
		return propertiesFrontendPageDataList;
	}

	private void validate(PropertiesFrontendPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PropertiesFrontendPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	private List<PropertiesFrontend> mapPropertiesFrontends(PageRequestDTO pageRequest,
			List<PropertiesFrontendPageData> pageData) {
		List<PropertiesFrontend> pageNavigators = pageData.stream()
				.map(frontend -> modelMapper.map(frontend, PropertiesFrontend.class)).collect(Collectors.toList());
		return pageNavigators;
	}
}
