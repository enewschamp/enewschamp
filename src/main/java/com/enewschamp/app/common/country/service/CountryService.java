package com.enewschamp.app.common.country.service;

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

import com.enewschamp.app.admin.country.repository.CountryRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.country.dto.CountryDTO;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.common.country.repository.CountryRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CountryPageData;
import com.google.common.reflect.TypeToken;

@Service
public class CountryService extends AbstractDomainService {

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CountryRepositoryCustomImpl countryRepositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Country create(Country countryEntity) {
		Country country = null;
		try {
			country = countryRepository.save(countryEntity);
		}
		catch(DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return country;
	}

	public Country update(Country countryEntity) {
		Long countryId = countryEntity.getCountryId();
		Country existingCountry = get(countryId);
		if (existingCountry.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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
			throw new BusinessException(ErrorCodeConstants.COUNTRY_NOT_FOUND);
		}
	}

	public List<CountryDTO> getAll() {
		List<Country> existingEntity = countryRepository.getAll();
		java.lang.reflect.Type listType = new TypeToken<List<CountryDTO>>() {
		}.getType();

		List<CountryDTO> countryDtoList = modelMapper.map(existingEntity, listType);
		return countryDtoList;
	}

	public List<CountryPageData> getCountries() {
		List<Country> existingEntity = countryRepository.getCountries();
		List<CountryPageData> countryPageDataList = new ArrayList<CountryPageData>();
		for (Country country : existingEntity) {
			CountryPageData countryData = new CountryPageData();
			countryData.setId(country.getNameId());
			countryData.setName(country.getDescription());
			countryPageDataList.add(countryData);
		}

		return countryPageDataList;
	}

	public CountryPageData getCountry(String countryId) {
		Optional<Country> existingEntity = countryRepository.getCountry(countryId);
		CountryPageData countryData = new CountryPageData();

		if (existingEntity.isPresent()) {
			Country country = existingEntity.get();
			countryData.setId(country.getNameId());
			countryData.setName(country.getDescription());

		}

		return countryData;
	}
	
	public Country read(Country countryEntity) {
		Long countryId = countryEntity.getCountryId();
		Country country = get(countryId);
        return country;
	}

	public Country close(Country countryEntity) {
		Long countryId = countryEntity.getCountryId();
		Country existingCountry = get(countryId);
		if(existingCountry.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingCountry.setRecordInUse(RecordInUseType.N);
		existingCountry.setOperationDateTime(null);
		return countryRepository.save(existingCountry);
	}
	
	public Country reInstate(Country countryEntity) {
		Long countryId = countryEntity.getCountryId();
		Country existingCountry = get(countryId);
		if(existingCountry.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingCountry.setRecordInUse(RecordInUseType.Y);
		existingCountry.setOperationDateTime(null);
		return countryRepository.save(existingCountry);
	}

	public Page<Country> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Country> countryList = countryRepositoryCustom.findAll(pageable, null);
		if(countryList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return countryList;
	}
}
