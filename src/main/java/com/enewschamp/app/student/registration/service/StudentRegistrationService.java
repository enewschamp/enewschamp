package com.enewschamp.app.student.registration.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.registration.repository.StudentRegistrationRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.registration.dto.StudentRegistrationDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.repository.StudentRegistrationRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentRegistrationService {

	@Autowired
	StudentRegistrationRepository repository;

	@Autowired
	StudentRegistrationRepositoryCustom repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public StudentRegistration create(StudentRegistration studentRegistration) {
		if (studentRegistration.getCreationDateTime() == null) {
			studentRegistration.setCreationDateTime(LocalDateTime.now());
		}
		StudentRegistration student = repository.save(studentRegistration);
		return student;
	}

	public StudentRegistrationDTO update(StudentRegistration studentRegistration) {
		Long studentId = studentRegistration.getStudentId();
		StudentRegistration existingStudentRegistration = get(studentId);
		modelMapper.map(studentRegistration, existingStudentRegistration);
		StudentRegistration student = repository.save(existingStudentRegistration);
		StudentRegistrationDTO studentDto = modelMapper.map(student, StudentRegistrationDTO.class);
		return studentDto;
	}

	public StudentRegistration get(Long studentRegistrationId) {
		Optional<StudentRegistration> existingEntity = repository.findById(studentRegistrationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUD_REG_NOT_FOUND, String.valueOf(studentRegistrationId));
		}
	}

	public StudentRegistration getStudentReg(String emailId) {
		Optional<StudentRegistration> existingEntity = repository.getStudent(emailId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
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

	public StudentRegistration read(StudentRegistration studentRegistrationEntity) {
		Long stakeHolderId = studentRegistrationEntity.getStudentId();
		StudentRegistration stakeHolder = get(stakeHolderId);
		return stakeHolder;
	}

	public StudentRegistration close(StudentRegistration studentRegistrationEntity) {
		Long stakeHolderId = studentRegistrationEntity.getStudentId();
		StudentRegistration existingStudentRegistration = get(stakeHolderId);
		if (existingStudentRegistration.getRecordInUse().equals(RecordInUseType.N)) {
			return existingStudentRegistration;
		}
		existingStudentRegistration.setRecordInUse(RecordInUseType.N);
		existingStudentRegistration.setOperationDateTime(null);
		return repository.save(existingStudentRegistration);
	}

	public StudentRegistration reInstate(StudentRegistration studentRegistrationEntity) {
		Long stakeHolderId = studentRegistrationEntity.getStudentId();
		StudentRegistration existingStudentRegistration = get(stakeHolderId);
		if (existingStudentRegistration.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingStudentRegistration;
		}
		existingStudentRegistration.setRecordInUse(RecordInUseType.Y);
		existingStudentRegistration.setOperationDateTime(null);
		return repository.save(existingStudentRegistration);
	}

	public Page<StudentRegistration> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentRegistration> stakeHolderList = repositoryCustom.findStudentRegistrations(pageable, searchRequest);
		return stakeHolderList;
	}
}
