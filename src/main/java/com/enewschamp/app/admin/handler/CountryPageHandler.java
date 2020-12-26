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
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.service.CityService;
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
		validate(pageData);
		Country country = mapCountryData(pageRequest, pageData);
		country = countryService.create(country);
		mapHeaderData(pageRequest, pageDto, pageData, country);
	//	pageData.setRecordInUse(country.getRecordInUse().toString());
		pageData.setCountryId(country.getCountryId());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, CountryPageData pageData, Country country) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageData.setLastUpdate(country.getOperationDateTime());
	}

	private Country mapCountryData(PageRequestDTO pageRequest, CountryPageData pageData) {
		Country country = modelMapper.map(pageData, Country.class);
	    country.setNameId(pageRequest.getData().get("name").asText());
		country.setOperatorId(pageRequest.getData().get("operator").asText());
		country.setRecordInUse(RecordInUseType.Y);
		return country;
	}

	@SneakyThrows
	private PageDTO updateCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		validate(pageData);
		Country country = mapCountryData(pageRequest, pageData);
		country.setCountryId(pageRequest.getData().get("countryId").asLong());
		country = countryService.update(country);
		mapHeaderData(pageRequest, pageDto, pageData, country);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		Country country = modelMapper.map(pageData, Country.class);
		country.setCountryId(pageData.getId());
		country = countryService.read(country);
		mapHeaderData(pageRequest, pageDto, pageData, country);
		mapPageData(pageData, country);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(CountryPageData pageData, Country country) {
		pageData.setCountryId(country.getCountryId());
		pageData.setName(country.getNameId());
		pageData.setDescription(country.getDescription());
		//pageData.setOperator(country.getOperatorId());
		//pageData.setRecordInUse(country.getRecordInUse().toString());
		pageData.setLastUpdate(country.getOperationDateTime());
		pageData.setIsd(country.getIsd());
		pageData.setCurrencyId(country.getCurrencyId());
		pageData.setLanguage(country.getLanguage());
	}

	@SneakyThrows
	private PageDTO closeCountry(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CountryPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CountryPageData.class);
		Country country = modelMapper.map(pageData, Country.class);
		country.setCountryId(pageData.getId());
		country = countryService.close(country);
		mapHeaderData(pageRequest, pageDto, pageData, country);
		mapPageData(pageData, country);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listCountry(PageRequestDTO pageRequest) {
		Page<Country> cityList = countryService.list(
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<CountryPageData> list = mapCountryData(cityList);
		List<PageData> variable = list
			    .stream()
			    .map(e -> (PageData) e)
			    .collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((cityList.getNumber() + 1) == cityList.getTotalPages()) {
			pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<CountryPageData> mapCountryData(Page<Country> page) {
		List<CountryPageData> countryPageDataList = new ArrayList<CountryPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<Country> pageDataList = page.getContent();
			for (Country country : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				CountryPageData statePageData = modelMapper.map(country, CountryPageData.class);
				statePageData.setName(country.getNameId());
				statePageData.setDescription(country.getDescription());
				//statePageData.setOperator(country.getOperatorId());
				statePageData.setLastUpdate(country.getOperationDateTime());
				countryPageDataList.add(statePageData);
			}
		}
		return countryPageDataList;
	}

	private void validate(CountryPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<CountryPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
