package com.enewschamp.app.admin.helpdesk.handler;

import java.time.LocalDateTime;
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
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.app.helpdesk.service.HelpdeskService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("HelpDeskPageHandler")
public class HelpDeskPageHandler implements IPageHandler {
	@Autowired
	private HelpdeskService helpDeskService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

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
		case "Reinstate":
			pageDto = reinstateHelpDesk(pageRequest);
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
		validate(pageData, this.getClass().getName());
		Helpdesk helpDesk = mapHelpDeskData(pageRequest, pageData);
		helpDesk.setCreateDateTime(LocalDateTime.now());
		helpDesk = helpDeskService.create(helpDesk);
		mapHelpdesk(pageRequest, pageDto, helpDesk);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		validate(pageData, this.getClass().getName());
		Helpdesk helpDesk = mapHelpDeskData(pageRequest, pageData);
		helpDesk = helpDeskService.update(helpDesk);
		mapHelpdesk(pageRequest, pageDto, helpDesk);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		Helpdesk helpDesk = modelMapper.map(pageData, Helpdesk.class);
		helpDesk = helpDeskService.read(helpDesk);
		mapHelpdesk(pageRequest, pageDto, helpDesk);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		Helpdesk helpDesk = modelMapper.map(pageData, Helpdesk.class);
		helpDesk = helpDeskService.close(helpDesk);
        mapHelpdesk(pageRequest, pageDto, helpDesk);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO reinstateHelpDesk(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		HelpDeskPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), HelpDeskPageData.class);
		Helpdesk helpDesk = modelMapper.map(pageData, Helpdesk.class);
		helpDesk = helpDeskService.reinstate(helpDesk);
		mapHelpdesk(pageRequest, pageDto, helpDesk);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listHelpDesk(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<Helpdesk> editionList = helpDeskService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<HelpDeskPageData> list = mapHelpDeskData(editionList);
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

	public List<HelpDeskPageData> mapHelpDeskData(Page<Helpdesk> page) {
		List<HelpDeskPageData> genrePageDataList = new ArrayList<HelpDeskPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Helpdesk> pageDataList = page.getContent();
			for (Helpdesk helpDesk : pageDataList) {
				HelpDeskPageData genrePageData = modelMapper.map(helpDesk, HelpDeskPageData.class);
				genrePageData.setLastUpdate(helpDesk.getOperationDateTime());
				genrePageDataList.add(genrePageData);
			}
		}
		return genrePageDataList;
	}

	private void mapHelpdesk(PageRequestDTO pageRequest, PageDTO pageDto, Helpdesk helpDesk) {
		HelpDeskPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(helpDesk);
		pageDto.setData(pageData);
	}


	private HelpDeskPageData mapPageData(Helpdesk helpdesk) {
		HelpDeskPageData pageData = modelMapper.map(helpdesk, HelpDeskPageData.class);
		pageData.setLastUpdate(helpdesk.getOperationDateTime());
		return pageData;
	}
	
	private Helpdesk mapHelpDeskData(PageRequestDTO pageRequest, HelpDeskPageData pageData) {
		Helpdesk helpDesk = modelMapper.map(pageData, Helpdesk.class);
		helpDesk.setRecordInUse(RecordInUseType.Y);
		return helpDesk;
	}
}
