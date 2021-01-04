package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.repository.StudentSchoolRepository;

@Service
public class StudentSchoolService {
	@Autowired
	StudentSchoolRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSchool create(StudentSchool StudentSchool) {
		return repository.save(StudentSchool);
	}

	public StudentSchool update(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentId();

		StudentSchool existingEntity = get(studentId);
		modelMapper.map(StudentSchool, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchool patch(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentId();

		StudentSchool existingEntity = get(studentId);
		modelMapperForPatch.map(StudentSchool, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchool get(Long studentId) {
		Optional<StudentSchool> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentSchool StudentSchool = new StudentSchool();
		StudentSchool.setStudentId(studentId);
		return auditService.getEntityAudit(StudentSchool);
	}

}
