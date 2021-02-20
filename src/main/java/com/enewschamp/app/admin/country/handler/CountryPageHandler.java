package com.enewschamp.app.admin.country.handler;

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

import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("CountryPageHandler")
@Slf4j
public class CountryPageHandler implements IPageHandler {
	@Autowired
	private CountryService countryService;
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
			pageDto = createCountry(pageRequest);
			break;
		case "Update":
			pageDto = updateCountry(pageRequest);
			break;
		case "Read":
			pageDto = readCountry(pageRequest);
			break;
		case "Close":
			pageDto = closeCountry(pageRequest);
			break;
		case "Reinstate":
			pageDto = reInstateCountry(pageRequest);
			break;
		case "List":
			pageDto = listCountry(pageRequest);
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
	private PageDTO createCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		validate(pageData, this.getClass().getName());
		Country country = mapCountryData(pageRequest, pageData);
		country = countryService.create(country);
		mapCountry(pageRequest, pageDto, country);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		validate(pageData,  this.getClass().getName());
		Country country = mapCountryData(pageRequest, pageData);
		country = countryService.update(country);
		mapCountry(pageRequest, pageDto, country);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		Country country = modelMapper.map(pageData, Country.class);
		country = countryService.read(country);
		mapCountry(pageRequest, pageDto, country);
		return pageDto;
	}
	
	@SneakyThrows
	private PageDTO closeCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		Country country = modelMapper.map(pageData, Country.class);
		country = countryService.close(country);
		mapCountry(pageRequest, pageDto, country);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reInstateCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		Country country = modelMapper.map(pageData, Country.class);
		country = countryService.reInstate(country);
		mapCountry(pageRequest, pageDto, country);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listCountry(PageRequestDTO pageRequest) {
		Page<Country> countryList = countryService.list(
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<CountryPageData> list = mapCountryData(countryList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((countryList.getNumber() + 1) == countryList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private void mapCountry(PageRequestDTO pageRequest, PageDTO pageDto, Country country) {
		CountryPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(country);
		pageDto.setData(pageData);
	}

	private Country mapCountryData(PageRequestDTO pageRequest, CountryPageData pageData) {
		Country country = modelMapper.map(pageData, Country.class);
		country.setRecordInUse(RecordInUseType.Y);
		return country;
	}

	private CountryPageData mapPageData(Country country) {
		CountryPageData pageData = modelMapper.map(country, CountryPageData.class);
		pageData.setLastUpdate(country.getOperationDateTime());
		return pageData;
	}


	public List<CountryPageData> mapCountryData(Page<Country> page) {
		List<CountryPageData> countryPageDataList = new ArrayList<CountryPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Country> pageDataList = page.getContent();
			for (Country country : pageDataList) {
				CountryPageData statePageData = modelMapper.map(country, CountryPageData.class);
				statePageData.setLastUpdate(country.getOperationDateTime());
				countryPageDataList.add(statePageData);
			}
		}
		return countryPageDataList;
	}
}
