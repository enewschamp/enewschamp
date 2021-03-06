package com.enewschamp.app.admin.state.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("StatePageHandler")
public class StatePageHandler implements IPageHandler {
	@Autowired
	private StateService stateService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

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
		case "Reinstate":
			pageDto = reInstate(pageRequest);
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
		validate(pageData, this.getClass().getName());
		State state = mapStateData(pageRequest, pageData);
		state = stateService.create(state);
		mapState(pageRequest, pageDto, state);
		return pageDto;
	}

	private State mapStateData(PageRequestDTO pageRequest, StatePageData pageData) {
		State state = modelMapper.map(pageData, State.class);
		state.setRecordInUse(RecordInUseType.Y);
		return state;
	}

	@SneakyThrows
	private PageDTO updateState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		validate(pageData, this.getClass().getName());
		State state = mapStateData(pageRequest, pageData);
		state = stateService.update(state);
		mapState(pageRequest, pageDto, state);
		return pageDto;
	}

	private void mapState(PageRequestDTO pageRequest, PageDTO pageDto, State state) {
		StatePageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(state);
		pageDto.setData(pageData);
	}

	@SneakyThrows
	private PageDTO readState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		State state = modelMapper.map(pageData, State.class);
		state = stateService.read(state);
		mapState(pageRequest, pageDto, state);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstate(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		State state = modelMapper.map(pageData, State.class);
		state = stateService.reInstate(state);
		mapState(pageRequest, pageDto, state);
		return pageDto;
	}

	private StatePageData mapPageData(State state) {
		StatePageData pageData = modelMapper.map(state, StatePageData.class);
		pageData.setLastUpdate(state.getOperationDateTime());
		return pageData;
	}

	@SneakyThrows
	private PageDTO closeState(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StatePageData pageData = objectMapper.readValue(pageRequest.getData().toString(), StatePageData.class);
		State state = modelMapper.map(pageData, State.class);
		state = stateService.close(state);
		mapState(pageRequest, pageDto, state);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listState(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<State> stateList = stateService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StatePageData> list = mapStateData(stateList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((stateList.getNumber() + 1) == stateList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<StatePageData> mapStateData(Page<State> page) {
		List<StatePageData> statePageDataList = new ArrayList<StatePageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<State> pageDataList = page.getContent();
			for (State state : pageDataList) {
				StatePageData statePageData = modelMapper.map(state, StatePageData.class);
				statePageData.setLastUpdate(state.getOperationDateTime());
				statePageDataList.add(statePageData);
			}
		}
		return statePageDataList;
	}

}
