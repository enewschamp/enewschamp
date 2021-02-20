package com.enewschamp.app.admin.city.handler;

import java.time.LocalDate;
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
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component("CityPageHandler")
public class CityPageHandler implements IPageHandler {
	@Autowired
	private CityService cityService;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createCity(pageRequest);
			break;
		case "Update":
			pageDto = updateCity(pageRequest);
			break;
		case "Read":
			pageDto = readCity(pageRequest);
			break;
		case "Close":
			pageDto = closeCity(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateCity(pageRequest);
			break;
		case "List":
			pageDto = listCity(pageRequest);
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
	private PageDTO createCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		validate(pageData, this.getClass().getName());
		City city = mapCityData(pageRequest, pageData);
		city = cityService.create(city);
		mapCity(pageRequest, pageDto, city);
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

	private City mapCityData(PageRequestDTO pageRequest, CityPageData pageData) {
		City city = modelMapper.map(pageData, City.class);
		city.setRecordInUse(RecordInUseType.Y);
		return city;
	}

	@SneakyThrows
	private PageDTO updateCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		validate(pageData, this.getClass().getName());
		City city = mapCityData(pageRequest, pageData);
		city = cityService.update(city);
		mapCity(pageRequest, pageDto, city);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		City city = modelMapper.map(pageData, City.class);
		city = cityService.read(city);
		mapCity(pageRequest, pageDto, city);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		City city = modelMapper.map(pageData, City.class);
		city = cityService.close(city);
		mapCity(pageRequest, pageDto, city);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		City city = modelMapper.map(pageData, City.class);
		city = cityService.reInstateCity(city);
		mapCity(pageRequest, pageDto, city);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listCity(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<City> cityList = cityService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<CityPageData> list = mapCityData(cityList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((cityList.getNumber() + 1) == cityList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private CityPageData mapPageData(City city) {
		CityPageData pageData = modelMapper.map(city, CityPageData.class);
		pageData.setLastUpdate(city.getOperationDateTime());
		return pageData;
	}

	private void mapCity(PageRequestDTO pageRequest, PageDTO pageDto, City city) {
		CityPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(city);
		pageDto.setData(pageData);
	}

	public List<CityPageData> mapCityData(Page<City> page) {
		List<CityPageData> cityPageDataList = new ArrayList<CityPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<City> pageDataList = page.getContent();
			for (City city : pageDataList) {
				CityPageData cityPageData = modelMapper.map(city, CityPageData.class);
				cityPageData.setLastUpdate(city.getOperationDateTime());
				cityPageDataList.add(cityPageData);
			}
		}
		return cityPageDataList;
	}
}
