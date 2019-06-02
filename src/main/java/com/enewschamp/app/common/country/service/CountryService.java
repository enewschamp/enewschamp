package com.enewschamp.app.common.country.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.country.dto.CountryDTO;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.common.country.repository.CountryRepository;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
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
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.COUNTRY_NOT_FOUND);
		}
	}
	public List<CountryDTO> getAll()
	{
		List<Country> existingEntity = countryRepository.getAll();
		java.lang.reflect.Type listType = new TypeToken<List<CountryDTO>>(){}.getType();

		List<CountryDTO> countryDtoList = modelMapper.map(existingEntity,listType);
		return countryDtoList;
	}
}
