package com.enewschamp.app.common.city.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.repository.CityRepository;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

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
			throw new BusinessException(ErrorCodes.CITY_NOT_FOUND);
		}
	}
	
	public List<City> getCitiesForCountry(String countryId)
	{
		return cityRepository.getCitiesByCountry(countryId);
	}
	
	public List<City> getCitiesForState(String stateId)
	{
		return cityRepository.getCitiesByState(stateId);
	}
	
	public List<ListOfValuesItem> getLOVForNewsEvents() {
		return toListOfValuesItems(cityRepository.getCityLOVForNewsEvent());
	}
	public City getCity(String cityId)
	{
		Optional<City> cityentity = cityRepository.getCity(cityId);
		
		if(cityentity.isPresent())
		{
			return cityentity.get(); 
		}else return null;
	}
	
}
