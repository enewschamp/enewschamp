package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentPreferencesWork;
import com.enewschamp.subscription.domain.repository.StudentPreferencesWorkRepository;

@Service
public class StudentPreferencesWorkService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPreferencesWorkRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentPreferencesWork create(StudentPreferencesWork studentPreferencesWork) {
		return repository.save(studentPreferencesWork);

	}

	public StudentPreferencesWork update(StudentPreferencesWork studentPreferencesWork) {
		Long studentId = studentPreferencesWork.getStudentId();

		StudentPreferencesWork existingEntity = get(studentId);
		modelMapper.map(studentPreferencesWork, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long studentId) {
		Optional<StudentPreferencesWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			repository.deleteById(studentId);
		}
	}

	public StudentPreferencesWork patch(StudentPreferencesWork studentPreferencesWork) {
		Long studentId = studentPreferencesWork.getStudentId();
		StudentPreferencesWork existingEntity = get(studentId);
		modelMapperForPatch.map(studentPreferencesWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPreferencesWork get(Long studentId) {
		Optional<StudentPreferencesWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentPreferencesWork studentPreferencesWork = new StudentPreferencesWork();
		studentPreferencesWork.setStudentId(studentId);
		return auditService.getEntityAudit(studentPreferencesWork);
	}
}
