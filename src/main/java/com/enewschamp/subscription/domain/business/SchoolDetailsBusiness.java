package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSchoolWork;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;

@Service
public class SchoolDetailsBusiness {

	@Autowired
	StudentSchoolService studentSchoolService;

	@Autowired
	StudentSchoolWorkService studentSchoolWorkService;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ModelMapper modelMapper;

	public StudentSchoolWork saveAsWork(StudentSchoolWorkDTO studentSchoolWorkDTO) {

		StudentSchoolWork studentSchoolWork = modelMapper.map(studentSchoolWorkDTO, StudentSchoolWork.class);
		studentSchoolWork = studentSchoolWorkService.create(studentSchoolWork);
		return studentSchoolWork;
	}

	public StudentSchool saveAsMaster(StudentSchoolDTO studentSchoolDTO) {
		StudentSchool studentSchool = modelMapper.map(studentSchoolDTO, StudentSchool.class);
		studentSchool = studentSchoolService.create(studentSchool);
		return studentSchool;
	}

	public void workToMaster(Long studentId) {
		StudentSchoolWork workEntity = studentSchoolWorkService.get(studentId);
		if (workEntity != null) {
			StudentSchool masterEntity = modelMapper.map(workEntity, StudentSchool.class);
			studentSchoolService.create(masterEntity);
		}
	}

	public StudentSchoolDTO getStudentFromMaster(Long studentId) {

		StudentSchool studentSchool = studentSchoolService.get(studentId);
		if (studentSchool != null) {
			StudentSchoolDTO studentSchooDTO = modelMapper.map(studentSchool, StudentSchoolDTO.class);
			return studentSchooDTO;
		} else
			return null;
	}

	public StudentSchoolWorkDTO getStudentFromWork(Long studentId) {
		StudentSchoolWork studentSchoolWork = studentSchoolWorkService.get(studentId);
		if (studentSchoolWork != null) {
			StudentSchoolWorkDTO studentSchoolWorkDTO = modelMapper.map(studentSchoolWork, StudentSchoolWorkDTO.class);
			return studentSchoolWorkDTO;
		} else
			return null;
	}
}