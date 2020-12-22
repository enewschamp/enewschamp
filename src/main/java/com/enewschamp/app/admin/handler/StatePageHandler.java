package com.enewschamp.app.admin.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StatePageHandler")
@Slf4j
public class StatePageHandler implements IPageHandler {
	@Autowired
	private StateService stateService;
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
			pageDto = createState(pageRequest);
			break;
		case "Update":
			pageDto = updateState(pageRequest);
			break;
		case "Read":
			pageDto = readState(pageRequest);
			break;
		case "Close":
			pageDto = closeState(pageRequest);
			break;
		case "List":
			pageDto = listState(pageRequest);
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
	private PageDTO createState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		validate(pageData);
		State state = mapStateData(pageRequest, pageData);
		state = stateService.create(state);
		mapHeaderData(pageRequest, pageDto, pageData, state);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, StatePageData pageData, State state) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageData.setId(state.getStateId());
		pageData.setLastUpdate(state.getOperationDateTime());
	}

	private State mapStateData(PageRequestDTO pageRequest, StatePageData pageData) {
		State state = modelMapper.map(pageData, State.class);
		state.setNameId(pageRequest.getData().get("name").asText());
		state.setOperatorId(pageRequest.getData().get("operator").asText());
		return state;
	}

	@SneakyThrows
	private PageDTO updateState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		validate(pageData);
		State state = mapStateData(pageRequest, pageData);
		state = stateService.update(state);
		mapHeaderData(pageRequest, pageDto, pageData, state);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		State state = modelMapper.map(pageData, State.class);
		state = stateService.read(state);
		mapHeaderData(pageRequest, pageDto, pageData, state);
		mapPageData(pageData, state);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(StatePageData pageData, State state) {
		pageData.setCountryId(state.getCountryId());
		pageData.setName(state.getNameId());
		pageData.setDescription(state.getDescription());
		pageData.setOperator(state.getOperatorId());
		pageData.setRecordInUse(state.getRecordInUse().toString());
		pageData.setLastUpdate(state.getOperationDateTime());
	}

	@SneakyThrows
	private PageDTO closeState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		State state = modelMapper.map(pageData, State.class);
		state = stateService.close(state);
		mapHeaderData(pageRequest, pageDto, pageData, state);
		mapPageData(pageData, state);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listState(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(pageRequest.getData().get("filter").get("countryId").asText());
		Page<State> stateList = stateService.list(searchRequest,
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<StatePageData> list = mapStateData(stateList);
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((stateList.getNumber() + 1) == stateList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(list);
		return dto;
	}

	public List<StatePageData> mapStateData(Page<State> page) {
		List<StatePageData> statePageDataList = new ArrayList<StatePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<State> pageDataList = page.getContent();
			for (State state : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				StatePageData statePageData = modelMapper.map(state, StatePageData.class);
				statePageData.setName(state.getNameId());
				statePageData.setDescription(state.getDescription());
				statePageData.setId(state.getStateId());
				statePageData.setOperator(state.getOperatorId());
				statePageData.setLastUpdate(state.getOperationDateTime());
				statePageDataList.add(statePageData);
			}
		}
		return statePageDataList;
	}

	private void validate(StatePageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StatePageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
