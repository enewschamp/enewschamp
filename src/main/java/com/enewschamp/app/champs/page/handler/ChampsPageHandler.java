package com.enewschamp.app.champs.page.handler;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.champs.page.data.ChampsPageData;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.service.StudentChampService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ChampsPageHandler")
public class ChampsPageHandler implements IPageHandler{

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	StudentChampService studentChampService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {

		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		ChampsSearchData searchData = new ChampsSearchData();
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(), ChampsSearchData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<ChampStudentDTO> champList = studentChampService.findChampStudents(searchData);
		//List<ChampStudentDTO> champList = studentChampService.findChampions(searchRequest)(searchData);
		ChampsPageData pageData = new ChampsPageData();
		pageData.setChamps(champList);
		pageData.setMonthYear(searchData.getMonthYear());
		pageData.setReadingLevel(searchData.getReadingLevel());
		
		pageDto.setData(pageData);
		
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
