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
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.helpdesk.entity.HelpDesk;
import com.enewschamp.app.helpdesk.service.HelpDeskService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("HelpDeskPageHandler")
@Slf4j
public class HelpDeskPageHandler implements IPageHandler {
	@Autowired
	private HelpDeskService helpDeskService;
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
			pageDto = createHelpDesk(pageRequest);
			break;
		case "Update":
			pageDto = updateHelpDesk(pageRequest);
			break;
		case "Read":
			pageDto = readHelpDesk(pageRequest);
			break;
		case "Close":
			pageDto = closeHelpDesk(pageRequest);
			break;
		case "List":
			pageDto = listHelpDesk(pageRequest);
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
	private PageDTO createHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		validate(pageData);
		HelpDesk helpDesk = mapHelpDeskData(pageRequest, pageData);
		helpDesk = helpDeskService.create(helpDesk);
		mapHeaderData(pageRequest, pageDto);
		pageData.setId(helpDesk.getRequestId());
		pageData.setLastUpdate(helpDesk.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
	}

	private HelpDesk mapHelpDeskData(PageRequestDTO pageRequest, HelpDeskPageData pageData) {
		HelpDesk helpDesk = modelMapper.map(pageData, HelpDesk.class);
		helpDesk.setOperatorId(pageRequest.getData().get("operator").asText());
		helpDesk.setRecordInUse(RecordInUseType.Y);
		return helpDesk;
	}

	@SneakyThrows
	private PageDTO updateHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		pageData.setRequestId(pageRequest.getData().get("id").asLong());
		validate(pageData);
		HelpDesk helpDesk = mapHelpDeskData(pageRequest, pageData);
		helpDesk = helpDeskService.update(helpDesk);
		mapHeaderData(pageRequest, pageDto);
		pageData.setLastUpdate(helpDesk.getOperationDateTime());
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		pageData.setRequestId(pageRequest.getData().get("id").asLong());
		HelpDesk helpDesk = modelMapper.map(pageData, HelpDesk.class);
		helpDesk = helpDeskService.read(helpDesk);
		mapHeaderData(pageRequest, pageDto);
		mapPageData(pageData, helpDesk);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(HelpDeskPageData pageData, HelpDesk helpDesk) {
		pageData.setId(helpDesk.getRequestId());
		pageData.setEditionId(helpDesk.getEditionId());
		//pageData.setRecordInUse(helpDesk.getRecordInUse().toString());
		//pageData.setOperator(helpDesk.getOperatorId());
		pageData.setLastUpdate(helpDesk.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		HelpDesk helpDesk = modelMapper.map(pageData, HelpDesk.class);
		helpDesk.setRequestId(pageData.getId());
		helpDesk = helpDeskService.close(helpDesk);
		mapHeaderData(pageRequest, pageDto);
		mapPageData(pageData, helpDesk);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listHelpDesk(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setStudentId(pageRequest.getData().get("filter").get("studentId").asText());
		searchRequest.setSupportUserId(pageRequest.getData().get("filter").get("supportUserId").asText());
		searchRequest.setCloseFlag(pageRequest.getData().get("filter").get("closeFlag").asText());
		searchRequest.setCategoryId(pageRequest.getData().get("filter").get("categoryId").asText());
		searchRequest.setCreateDateTo(pageRequest.getData().get("filter").get("createDateTo").asText());
		searchRequest.setCreateDateFrom(pageRequest.getData().get("filter").get("createDateFrom").asText());
		Page<HelpDesk> editionList = helpDeskService.list(searchRequest,
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<HelpDeskPageData> list = mapHelpDeskData(editionList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((editionList.getNumber() + 1) == editionList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<HelpDeskPageData> mapHelpDeskData(Page<HelpDesk> page) {
		List<HelpDeskPageData> genrePageDataList = new ArrayList<HelpDeskPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<HelpDesk> pageDataList = page.getContent();
			for (HelpDesk helpDesk : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				HelpDeskPageData genrePageData = modelMapper.map(helpDesk, HelpDeskPageData.class);
				genrePageData.setId(helpDesk.getRequestId());
			//	genrePageData.setOperator(helpDesk.getOperatorId());
				genrePageData.setLastUpdate(helpDesk.getOperationDateTime());
				genrePageDataList.add(genrePageData);
			}
		}
		return genrePageDataList;
	}

	private void validate(HelpDeskPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<HelpDeskPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
