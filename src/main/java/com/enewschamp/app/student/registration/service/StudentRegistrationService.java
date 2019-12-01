package com.enewschamp.app.student.registration.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.registration.dto.StudentRegistrationDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.repository.StudentRegistrationRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentRegistrationService {
	
	@Autowired
	StudentRegistrationRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	
	public StudentRegistration create(StudentRegistration studentRegistration) {
		StudentRegistration student =  repository.save(studentRegistration);
		
		return student;
	}
	
	public StudentRegistrationDTO update(StudentRegistration studentRegistration) {
		Long StudentRegistrationId = studentRegistration.getStudRegId();
		StudentRegistration existingStudentRegistration = get(StudentRegistrationId);
		modelMapper.map(studentRegistration, existingStudentRegistration);
		StudentRegistration student = repository.save(existingStudentRegistration);
		StudentRegistrationDTO studentDto = modelMapper.map(student, StudentRegistrationDTO.class);

		return studentDto;
	}
	
	public StudentRegistration get(Long StudentRegistrationId) {
		Optional<StudentRegistration> existingEntity = repository.findById(StudentRegistrationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_REG_NOT_FOUND, String.valueOf(StudentRegistrationId));
		}
	}

	public StudentRegistration getStudentReg(String emailId) {
		Optional<StudentRegistration> existingEntity = repository.getStudent(emailId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_REG_NOT_FOUND, String.valueOf(emailId));
		}
	}
	public boolean userExists(String emailId) {
		boolean exists = false;
		Optional<StudentRegistration> existingEntity = repository.getStudent(emailId);
		if (existingEntity.isPresent()) {
			exists = true;
		}
		return exists;
	}
}
