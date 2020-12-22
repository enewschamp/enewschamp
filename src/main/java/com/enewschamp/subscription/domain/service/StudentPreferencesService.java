package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.repository.StudentPreferencesRepository;

@Service
public class StudentPreferencesService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPreferencesRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentPreferences create(StudentPreferences studentPreferences) {
		return repository.save(studentPreferences);

	}

	public StudentPreferences update(StudentPreferences studentPreferences) {
		Long studentId = studentPreferences.getStudentId();
		StudentPreferences existingEntity = get(studentId);
		modelMapper.map(studentPreferences, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPreferences patch(StudentPreferences studentPreferences) {
		Long studentId = studentPreferences.getStudentId();
		StudentPreferences existingEntity = get(studentId);
		modelMapperForPatch.map(studentPreferences, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPreferences get(Long studentId) {
		Optional<StudentPreferences> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentPreferences studentPreferences = new StudentPreferences();
		studentPreferences.setStudentId(studentId);
		return auditService.getEntityAudit(studentPreferences);
	}
}
