package com.enewschamp.app.admin.handler;

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
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.service.EditionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("EditionPageHandler")
@Slf4j
public class EditionPageHandler implements IPageHandler {
	@Autowired
	private EditionService editionService;
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
			pageDto = createEdition(pageRequest);
			break;
		case "Update":
			pageDto = updateEdition(pageRequest);
			break;
		case "Read":
			pageDto = readEdition(pageRequest);
			break;
		case "Close":
			pageDto = closeEdition(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateEdition(pageRequest);
			break;
		case "List":
			pageDto = listEdition(pageRequest);
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
	private PageDTO createEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EditionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EditionPageData.class);
		validate(pageData);
		Edition edition = mapEditionData(pageRequest, pageData);
		edition = editionService.create(edition);
        mapEdition(pageRequest, pageDto, edition);
		return pageDto;
	}


	@SneakyThrows
	private PageDTO updateEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EditionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EditionPageData.class);
		validate(pageData);
		Edition edition = mapEditionData(pageRequest, pageData);
		edition = editionService.update(edition);
        mapEdition(pageRequest, pageDto, edition);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EditionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EditionPageData.class);
		Edition edition = modelMapper.map(pageData, Edition.class);
		edition = editionService.read(edition);
        mapEdition(pageRequest, pageDto, edition);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO closeEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EditionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EditionPageData.class);
		Edition edition = modelMapper.map(pageData, Edition.class);
		edition = editionService.close(edition);
        mapEdition(pageRequest, pageDto, edition);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO reinstateEdition(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		EditionPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), EditionPageData.class);
		Edition edition = modelMapper.map(pageData, Edition.class);
		edition = editionService.reinstate(edition);
        mapEdition(pageRequest, pageDto, edition);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listEdition(PageRequestDTO pageRequest) {
		Page<Edition> editionList = editionService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<EditionPageData> list = mapEditionData(editionList);
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

	public List<EditionPageData> mapEditionData(Page<Edition> page) {
		List<EditionPageData> editionPageDataList = new ArrayList<EditionPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Edition> pageDataList = page.getContent();
			for (Edition edition : pageDataList) {
				EditionPageData editionPageData = modelMapper.map(edition, EditionPageData.class);
				editionPageData.setLastUpdate(edition.getOperationDateTime());
				editionPageDataList.add(editionPageData);
			}
		}
		return editionPageDataList;
	}

	private void mapEdition(PageRequestDTO pageRequest, PageDTO pageDto, Edition edition) {
		EditionPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(edition);
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

	private Edition mapEditionData(PageRequestDTO pageRequest, EditionPageData pageData) {
		Edition edition = modelMapper.map(pageData, Edition.class);
		edition.setRecordInUse(RecordInUseType.Y);
		return edition;
	}

	private EditionPageData mapPageData(Edition edition) {
		EditionPageData pageData = modelMapper.map(edition, EditionPageData.class);
		pageData.setLastUpdate(edition.getOperationDateTime());
		return pageData;
	}
	
	private void validate(EditionPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<EditionPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
