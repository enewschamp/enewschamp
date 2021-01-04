package com.enewschamp.app.common.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.repository.CityRepository;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CityPageData;
import com.enewschamp.subscription.app.dto.CountryPageData;

@Service
public class CityService extends AbstractDomainService {

	@Autowired
	CityRepository cityRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public City create(City CityEntity) {
		return cityRepository.save(CityEntity);
	}

	public City update(City CityEntity) {
		Long CityId = CityEntity.getCityId();
		City existingCity = get(CityId);
		modelMapper.map(CityEntity, existingCity);
		return cityRepository.save(existingCity);
	}

	public City patch(City City) {
		Long navId = City.getCityId();
		City existingEntity = get(navId);
		modelMapperForPatch.map(City, existingEntity);
		return cityRepository.save(existingEntity);
	}

	public void delete(Long CityId) {
		cityRepository.deleteById(CityId);
	}

	public City get(Long CityId) {
		Optional<City> existingEntity = cityRepository.findById(CityId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.CITY_NOT_FOUND);
		}
	}

	public List<City> getCitiesForCountry(String countryId) {
		return cityRepository.getCitiesByCountry(countryId);
	}

	public List<City> getCitiesForState(String stateId, String countryId) {
		return cityRepository.getCitiesByState(stateId, countryId);
	}

	public List<CityPageData> getCitiesForStateCountry(String stateId, String countryId) {
		List<City> existingEntity = cityRepository.getCitiesByState(stateId, countryId);
		List<CityPageData> cityPageDataList = new ArrayList<CityPageData>();
		for (City city : existingEntity) {
			CityPageData cityData = new CityPageData();
			cityData.setId(city.getNameId());
			cityData.setName(city.getDescription());
			cityPageDataList.add(cityData);
		}
		return cityPageDataList;
	}

	public List<ListOfValuesItem> getLOVForNewsEvents() {
		return toListOfValuesItems(cityRepository.getCityLOVForNewsEvent());
	}

	public City getCity(String cityId) {
		Optional<City> cityentity = cityRepository.getCity(cityId);

		if (cityentity.isPresent()) {
			return cityentity.get();
		} else
			return null;
	}

}
