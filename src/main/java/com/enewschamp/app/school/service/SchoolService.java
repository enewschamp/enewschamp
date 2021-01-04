package com.enewschamp.app.school.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.repository.SchoolProgramRepository;
import com.enewschamp.app.school.repository.SchoolRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.SchoolData;
import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

@Service
public class SchoolService {

	@Autowired
	SchoolRepository schoolRepository;

	@Autowired
	SchoolProgramService schoolProgramService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public School create(School SchoolEntity) {
		return schoolRepository.save(SchoolEntity);
	}

	public School update(School SchoolEntity) {
		Long SchoolId = SchoolEntity.getSchoolId();
		School existingSchool = get(SchoolId);
		modelMapper.map(SchoolEntity, existingSchool);
		return schoolRepository.save(existingSchool);
	}

	public School patch(School School) {
		Long navId = School.getSchoolId();
		School existingEntity = get(navId);
		modelMapperForPatch.map(School, existingEntity);
		return schoolRepository.save(existingEntity);
	}

	public void delete(Long SchoolId) {
		schoolRepository.deleteById(SchoolId);
	}

	public School get(Long SchoolId) {
		Optional<School> existingEntity = schoolRepository.findById(SchoolId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_NOT_FOUND);
		}
	}

	public List<School> getSchools(String cityId, String stateId, String countryId) {
		return schoolRepository.getSchools(cityId, stateId, countryId);
	}

	public List<SchoolData> getSchoolsForCityStateCountry(String cityId, String stateId, String countryId) {
		List<School> existingEntity = schoolRepository.getSchools(cityId, stateId, countryId);
		List<SchoolData> schoolPageDataList = new ArrayList<SchoolData>();
		for (School school : existingEntity) {
			SchoolData schoolData = new SchoolData();
			schoolData.setId(school.getSchoolId());
			schoolData.setName(school.getName());
			schoolData.setSchoolProgramCode(school.getSchoolProgramCode());
			schoolPageDataList.add(schoolData);
		}
		return schoolPageDataList;
	}

	public List<SchoolProgramLOV> getSchoolProgramDetails() {
		return schoolProgramService.getSchoolProgramDetails();
	}

	public List<School> getSchoolFromProgramCode(String schoolProgramCode) {
		return schoolRepository.getSchoolFromProgramCode(schoolProgramCode);
	}
}
