package com.enewschamp.app.school.service;

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
import com.enewschamp.app.admin.school.repository.SchoolRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.repository.SchoolRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.SchoolData;
import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

@Service
public class SchoolService {

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private SchoolRepositoryCustom schoolRepositoryCustom;

	@Autowired
	private SchoolProgramService schoolProgramService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public School create(School schoolEntity) {
		School school = null;
		try {
			school = schoolRepository.save(schoolEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return school;
	}

	public School update(School SchoolEntity) {
		Long SchoolId = SchoolEntity.getSchoolId();
		School existingSchool = get(SchoolId);
		if (existingSchool.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public School read(School schoolEntity) {
		Long countryId = schoolEntity.getSchoolId();
		School school = get(countryId);
		return school;
	}

	public School close(School schoolEntity) {
		Long schoolId = schoolEntity.getSchoolId();
		School existingSchool = get(schoolId);
		if (existingSchool.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingSchool.setRecordInUse(RecordInUseType.N);
		existingSchool.setOperationDateTime(null);
		return schoolRepository.save(existingSchool);
	}

	public School reInstate(School schoolEntity) {
		Long schoolId = schoolEntity.getSchoolId();
		School existingSchool = get(schoolId);
		if (existingSchool.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingSchool.setRecordInUse(RecordInUseType.Y);
		existingSchool.setOperationDateTime(null);
		return schoolRepository.save(existingSchool);
	}

	public Page<School> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<School> schoolList = schoolRepositoryCustom.findSchools(pageable, searchRequest);
		if (schoolList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return schoolList;
	}

	public School getByNameAndCityAndStateAndCountryId(String nameId, String cityId, String stateId, String countryId) {
		Optional<School> school = schoolRepository.findByNameAndCityIdAndStateIdAndCountryId(nameId, cityId, stateId,
				countryId);
		if (school.isPresent()) {
			return school.get();
		}
		return null;
	}
}
