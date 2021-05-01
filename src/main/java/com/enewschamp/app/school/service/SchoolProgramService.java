package com.enewschamp.app.school.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.school.repository.SchoolProgramRepository;
import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

@Service
public class SchoolProgramService {

	@Autowired
	SchoolProgramRepository schoolProgramRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public List<SchoolProgramLOV> getSchoolProgramDetails() {
		return schoolProgramRepository.getSchoolsForProgramCode();
	}

}
