package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentDetailsWork;
import com.enewschamp.subscription.domain.repository.StudentDetailsWorkRepository;

@Service
public class StudentDetailsWorkService {

	@Autowired
	StudentDetailsWorkRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentDetailsWork create(StudentDetailsWork StudentDetailsWork) {
		return repository.save(StudentDetailsWork);
	}

	public StudentDetailsWork update(StudentDetailsWork StudentDetailsWork) {
		Long studentId = StudentDetailsWork.getStudentId();

		StudentDetailsWork existingEntity = get(studentId);
		modelMapper.map(StudentDetailsWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentDetailsWork patch(StudentDetailsWork StudentDetailsWork) {
		Long studentId = StudentDetailsWork.getStudentId();

		StudentDetailsWork existingEntity = get(studentId);
		modelMapperForPatch.map(StudentDetailsWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentDetailsWork get(Long studentId) {
		Optional<StudentDetailsWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public void delete(Long studentId) {
		Optional<StudentDetailsWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			repository.deleteById(studentId);
		}
	}

	public String getAudit(Long studentId) {
		StudentDetailsWork StudentDetailsWork = new StudentDetailsWork();
		StudentDetailsWork.setStudentId(studentId);
		return auditService.getEntityAudit(StudentDetailsWork);
	}

}