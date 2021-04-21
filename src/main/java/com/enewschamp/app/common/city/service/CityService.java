package com.enewschamp.app.common.city.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.city.repository.CityRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.repository.CityRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CityPageData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CityService extends AbstractDomainService {

	@Autowired
	CityRepository cityRepository;

	@Autowired
	private CityRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public City create(City cityEntity) {
		City city = null;
		try {
			city = cityRepository.save(cityEntity);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return city;
	}

	public City update(City CityEntity) {
		Long CityId = CityEntity.getCityId();
		City existingCity = get(CityId);
		if (existingCity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public City get(Long cityId) {
		Optional<City> existingEntity = cityRepository.findById(cityId);
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

	public City read(City cityEntity) {
		Long cityId = cityEntity.getCityId();
		City city = get(cityId);
		return city;

	}

	public City close(City cityEntity) {
		Long cityId = cityEntity.getCityId();
		City existingCity = get(cityId);
		if (existingCity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingCity.setRecordInUse(RecordInUseType.N);
		existingCity.setOperationDateTime(null);
		return cityRepository.save(existingCity);
	}

	public City reInstateCity(City cityEntity) {
		Long cityId = cityEntity.getCityId();
		City existingCity = get(cityId);
		if (existingCity.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingCity.setRecordInUse(RecordInUseType.Y);
		existingCity.setOperationDateTime(null);
		return cityRepository.save(existingCity);
	}

	public Page<City> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<City> cityList = customRepository.findAll(pageable, searchRequest);
		return cityList;
	}

	public City getByNameAndCountryId(String nameId, String stateId, String countryId) {
		Optional<City> city = cityRepository.findByNameIdAndStateIdAndCountryId(nameId, stateId, countryId);
		if (city.isPresent())
			return city.get();
		else
			return null;
	}

}