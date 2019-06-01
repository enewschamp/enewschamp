package com.enewschamp.app.school.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.repository.SchoolRepository;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Service
public class SchoolService {

	@Autowired
	SchoolRepository SchoolRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public School create(School SchoolEntity) {
		return SchoolRepository.save(SchoolEntity);
	}

	public School update(School SchoolEntity) {
		Long SchoolId = SchoolEntity.getSchoolId();
		School existingSchool = get(SchoolId);
		modelMapper.map(SchoolEntity, existingSchool);
		return SchoolRepository.save(existingSchool);
	}

	public School patch(School School) {
		Long navId = School.getSchoolId();
		School existingEntity = get(navId);
		modelMapperForPatch.map(School, existingEntity);
		return SchoolRepository.save(existingEntity);
	}

	public void delete(Long SchoolId) {
		SchoolRepository.deleteById(SchoolId);
	}

	public School get(Long SchoolId) {
		Optional<School> existingEntity = SchoolRepository.findById(SchoolId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.SCHOOL_NOT_FOUND,
					"School not found!");
		}
	}
	
	public List<School> getSchools(String cityId, String stateId, String countryId)
	{
		return getSchools( cityId,  stateId,  countryId);
	}
}
