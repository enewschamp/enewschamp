package com.enewschamp.app.common.country.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.country.dto.CountryDTO;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.common.country.repository.CountryRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CountryPageData;
import com.google.common.reflect.TypeToken;

@Service
public class CountryService {

	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public Country create(Country countryEntity) {
		return countryRepository.save(countryEntity);
	}

	public Country update(Country countryEntity) {
		Long countryId = countryEntity.getCountryId();
		Country existingCountry = get(countryId);
		modelMapper.map(countryEntity, existingCountry);
		return countryRepository.save(existingCountry);
	}

	public Country patch(Country Country) {
		Long navId = Country.getCountryId();
		Country existingEntity = get(navId);
		modelMapperForPatch.map(Country, existingEntity);
		return countryRepository.save(existingEntity);
	}

	public void delete(Long countryId) {
		countryRepository.deleteById(countryId);
	}

	public Country get(Long countryId) {
		Optional<Country> existingEntity = countryRepository.findById(countryId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.COUNTRY_NOT_FOUND);
		}
	}
	public List<CountryDTO> getAll()
	{
		List<Country> existingEntity = countryRepository.getAll();
		java.lang.reflect.Type listType = new TypeToken<List<CountryDTO>>(){}.getType();

		List<CountryDTO> countryDtoList = modelMapper.map(existingEntity,listType);
		return countryDtoList;
	}
	public List<CountryPageData> getCountries()
	{
		List<Country> existingEntity = countryRepository.getCountries();
		List<CountryPageData> countryPageDataList = new ArrayList<CountryPageData>();
		for(Country country:existingEntity)
		{
			CountryPageData countryData = new CountryPageData();
			countryData.setId(country.getNameId());
			countryData.setName(country.getDescription());
			countryPageDataList.add(countryData);
		}
		
		return countryPageDataList;
	}
	public CountryPageData getCountry(String countryId)
	{
		Optional<Country> existingEntity = countryRepository.getCountry(countryId);
		CountryPageData countryData = new CountryPageData();

		if(existingEntity.isPresent())
		{
			Country country = existingEntity.get();
			countryData.setId(country.getNameId());
			countryData.setName(country.getDescription());
			
		}
		
		return countryData;
	}
}
