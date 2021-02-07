package com.enewschamp.app.admin.properties.backend.handler;

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
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("PropertiesBackendPageHandler")
@Transactional
@Slf4j
public class PropertiesBackendPageHandler implements IPageHandler {
	@Autowired
	private PropertiesBackendService propertiesBackendService;
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
			pageDto = createPropertiesBackend(pageRequest);
			break;
		case "Update":
			pageDto = updatePropertiesBackend(pageRequest);
			break;
		case "Read":
			pageDto = readPropertiesBackend(pageRequest);
			break;
		case "Close":
			pageDto = closePropertiesBackend(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstatePropertiesBackend(pageRequest);
			break;
		case "List":
			pageDto = listPropertiesBackend(pageRequest);
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
	private PageDTO createPropertiesBackend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesBackendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesBackendPageData.class);
		validate(pageData);
		PropertiesBackend propertiesBackend = mapPropertiesBackendData(pageRequest, pageData);
		propertiesBackend = propertiesBackendService.create(propertiesBackend);
		mapPropertiesBackend(pageRequest, pageDto, propertiesBackend);
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

	private PropertiesBackend mapPropertiesBackendData(PageRequestDTO pageRequest, PropertiesBackendPageData pageData) {
		PropertiesBackend propertiesBackend = modelMapper.map(pageData, PropertiesBackend.class);
		propertiesBackend.setRecordInUse(RecordInUseType.Y);
		return propertiesBackend;
	}

	@SneakyThrows
	private PageDTO updatePropertiesBackend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesBackendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesBackendPageData.class);
		validate(pageData);
		PropertiesBackend propertiesBackend = mapPropertiesBackendData(pageRequest, pageData);
		propertiesBackend = propertiesBackendService.update(propertiesBackend);
		mapPropertiesBackend(pageRequest, pageDto, propertiesBackend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readPropertiesBackend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesBackendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesBackendPageData.class);
		PropertiesBackend propertiesBackend = modelMapper.map(pageData, PropertiesBackend.class);
		propertiesBackend = propertiesBackendService.read(propertiesBackend);
		mapPropertiesBackend(pageRequest, pageDto, propertiesBackend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closePropertiesBackend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesBackendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesBackendPageData.class);
		PropertiesBackend propertiesBackend = modelMapper.map(pageData, PropertiesBackend.class);
		propertiesBackend = propertiesBackendService.close(propertiesBackend);
		mapPropertiesBackend(pageRequest, pageDto, propertiesBackend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstatePropertiesBackend(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		PropertiesBackendPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				PropertiesBackendPageData.class);
		PropertiesBackend propertiesBackend = modelMapper.map(pageData, PropertiesBackend.class);
		propertiesBackend = propertiesBackendService.reinstate(propertiesBackend);
		mapPropertiesBackend(pageRequest, pageDto, propertiesBackend);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listPropertiesBackend(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<PropertiesBackend> propertiesBackendList = propertiesBackendService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<PropertiesBackendPageData> list = mapPropertiesBackendData(propertiesBackendList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((propertiesBackendList.getNumber() + 1) == propertiesBackendList.getTotalPages()) {
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
		List<PropertiesBackendPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<PropertiesBackendPageData>>() {
				});
		List<PropertiesBackend> pageNavigators = mapPropertiesBackend(pageRequest, pageData);
		int totalRecords = propertiesBackendService.createAll(pageNavigators);
		BulkInsertResponsePageData responseData = new BulkInsertResponsePageData();
		responseData.setNumberOfRecords(totalRecords);
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.setData(responseData);
		return pageDto;
	}

	private PropertiesBackendPageData mapPageData(PropertiesBackend propertiesBackend) {
		PropertiesBackendPageData pageData = modelMapper.map(propertiesBackend, PropertiesBackendPageData.class);
		pageData.setLastUpdate(propertiesBackend.getOperationDateTime());
		return pageData;
	}

	private void mapPropertiesBackend(PageRequestDTO pageRequest, PageDTO pageDto,
			PropertiesBackend propertiesBackend) {
		PropertiesBackendPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(propertiesBackend);
		pageDto.setData(pageData);
	}

	public List<PropertiesBackendPageData> mapPropertiesBackendData(Page<PropertiesBackend> page) {
		List<PropertiesBackendPageData> propertiesBackendPageDataList = new ArrayList<PropertiesBackendPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<PropertiesBackend> pageDataList = page.getContent();
			for (PropertiesBackend propertiesBackend : pageDataList) {
				PropertiesBackendPageData propertiesBackendPageData = modelMapper.map(propertiesBackend,
						PropertiesBackendPageData.class);
				propertiesBackendPageData.setLastUpdate(propertiesBackend.getOperationDateTime());
				propertiesBackendPageDataList.add(propertiesBackendPageData);
			}
		}
		return propertiesBackendPageDataList;
	}

	private void validate(PropertiesBackendPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<PropertiesBackendPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	private List<PropertiesBackend> mapPropertiesBackend(PageRequestDTO pageRequest,
			List<PropertiesBackendPageData> pageData) {
		List<PropertiesBackend> propertiesBackends = pageData.stream()
				.map(backend -> modelMapper.map(backend, PropertiesBackend.class)).collect(Collectors.toList());
		return propertiesBackends;
	}
}