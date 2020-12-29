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
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("CityPageHandler")
@Slf4j
public class CityPageHandler implements IPageHandler {
	@Autowired
	private CityService cityService;
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
		validate(pageData);
		City city = mapCityData(pageRequest, pageData);
		city = cityService.create(city);
		mapHeaderData(pageRequest, pageDto, pageData, city);
		//pageData.setRecordInUse(city.getRecordInUse().toString());
		pageData.setIsApplicableForNewsEvents(city.getIsApplicableForNewsEvents());
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto, CityPageData pageData, City city) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageData.setId(city.getCityId());
		pageData.setLastUpdate(city.getOperationDateTime());
	}

	private City mapCityData(PageRequestDTO pageRequest, CityPageData pageData) {
		City city = modelMapper.map(pageData, City.class);
	    city.setNameId(pageRequest.getData().get("name").asText());
		city.setOperatorId(pageRequest.getData().get("operator").asText());
		city.setRecordInUse(RecordInUseType.Y);
		city.setIsApplicableForNewsEvents(pageRequest.getData().get("isApplicableForNewsEvents").asText());
		return city;
	}

	@SneakyThrows
	private PageDTO updateCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		validate(pageData);
		City city = mapCityData(pageRequest, pageData);
		city.setCityId(pageRequest.getData().get("id").asLong());
		city = cityService.update(city);
		mapHeaderData(pageRequest, pageDto, pageData, city);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		City city = modelMapper.map(pageData, City.class);
		city = cityService.read(city);
		mapHeaderData(pageRequest, pageDto, pageData, city);
		mapPageData(pageData, city);
		pageDto.setData(pageData);
		return pageDto;
	}

	private void mapPageData(CityPageData pageData, City city) {
		pageData.setCountryId(city.getCountryId());
		pageData.setName(city.getNameId());
		pageData.setDescription(city.getDescription());
		//pageData.setOperator(city.getOperatorId());
		//pageData.setRecordInUse(city.getRecordInUse().toString());
		pageData.setLastUpdate(city.getOperationDateTime());
		pageData.setStateId(city.getStateId());
		pageData.setIsApplicableForNewsEvents(city.getIsApplicableForNewsEvents());
	}

	@SneakyThrows
	private PageDTO closeCity(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		CityPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), CityPageData.class);
		City city = modelMapper.map(pageData, City.class);
		city = cityService.close(city);
		mapHeaderData(pageRequest, pageDto, pageData, city);
		mapPageData(pageData, city);
		pageDto.setData(pageData);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listCity(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = new AdminSearchRequest();
		searchRequest.setCountryId(pageRequest.getData().get("filter").get("countryId").asText());
		searchRequest.setStateId(pageRequest.getData().get("filter").get("stateId").asText());
		searchRequest.setName(pageRequest.getData().get("filter").get("name").asText());
		searchRequest.setNewsEventsApplicable(pageRequest.getData().get("filter").get("newsEventsApplicable").asText());
		Page<City> cityList = cityService.list(searchRequest,
				pageRequest.getData().get("pagination").get("pageNumber").asInt(),
				pageRequest.getData().get("pagination").get("pageSize").asInt());

		List<CityPageData> list = mapCityData(cityList);
		List<PageData> variable = list
			    .stream()
			    .map(e -> (PageData) e)
			    .collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		dto.setHeader(pageRequest.getHeader());
		if ((cityList.getNumber() + 1) == cityList.getTotalPages()) {
			//pageData.getPagination().setLastPage(true);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	public List<CityPageData> mapCityData(Page<City> page) {
		List<CityPageData> cityPageDataList = new ArrayList<CityPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<City> pageDataList = page.getContent();
			for (City city : pageDataList) {
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				CityPageData statePageData = modelMapper.map(city, CityPageData.class);
				statePageData.setName(city.getNameId());
				statePageData.setDescription(city.getDescription());
				statePageData.setId(city.getCityId());
				//statePageData.setOperator(city.getOperatorId());
				statePageData.setLastUpdate(city.getOperationDateTime());
				cityPageDataList.add(statePageData);
			}
		}
		return cityPageDataList;
	}

	private void validate(CityPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<CityPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.info(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
}
