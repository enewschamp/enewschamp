package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentSchoolWork;
import com.enewschamp.subscription.domain.repository.StudentSchoolWorkRepository;

@Service
public class StudentSchoolWorkService {
	@Autowired
	StudentSchoolWorkRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSchoolWork create(StudentSchoolWork StudentSchoolWork) {
		return repository.save(StudentSchoolWork);
	}

	public StudentSchoolWork update(StudentSchoolWork StudentSchoolWork) {
		Long studentId = StudentSchoolWork.getStudentId();

		StudentSchoolWork existingEntity = get(studentId);
		modelMapper.map(StudentSchoolWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchoolWork patch(StudentSchoolWork StudentSchoolWork) {
		Long studentId = StudentSchoolWork.getStudentId();

		StudentSchoolWork existingEntity = get(studentId);
		modelMapperForPatch.map(StudentSchoolWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchoolWork get(Long studentId) {
		Optional<StudentSchoolWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentSchoolWork StudentSchoolWork = new StudentSchoolWork();
		StudentSchoolWork.setStudentId(studentId);
		return auditService.getEntityAudit(StudentSchoolWork);
	}

	public void delete(Long studentId) {
		Optional<StudentSchoolWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			repository.deleteById(studentId);
		}
	}

}
