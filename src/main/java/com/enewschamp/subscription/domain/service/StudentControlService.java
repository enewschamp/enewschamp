package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.repository.StudentControlRepository;

@Service
public class StudentControlService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	StudentControlRepository repository;

	public StudentControl create(StudentControl studentControl) {
		return repository.save(studentControl);
	}

	public StudentControl update(StudentControl StudentControl) {
		Long studentId = StudentControl.getStudentId();

		StudentControl existingEntity = get(studentId);
		modelMapper.map(StudentControl, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControl patch(StudentControl StudentControl) {
		Long studentId = StudentControl.getStudentId();

		StudentControl existingEntity = get(studentId);
		modelMapperForPatch.map(StudentControl, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControl get(Long studentId) {
		Optional<StudentControl> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public boolean studentExist(String emailId) {
		StudentControl existingEntity = repository.searchByEmail(emailId);
		if (existingEntity != null) {
			return true;
		} else {
			return false;
		}
	}

	public StudentControl getStudentByEmail(String emailId) {
		StudentControl existingEntity = repository.searchByEmail(emailId);
		return existingEntity;

	}

	public boolean studentExist(Long studentId) {
		return repository.existsById(studentId);

	}

	public String getAudit(Long studentId) {
		StudentControl StudentControl = new StudentControl();
		StudentControl.setStudentId(studentId);
		return auditService.getEntityAudit(StudentControl);
	}
}
