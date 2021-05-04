package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.preference.repository.StudentPreferencesRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.repository.StudentPreferencesRepository;

@Service
public class StudentPreferencesService {

	@Autowired
	StudentPreferencesRepository repository;

	@Autowired
	private StudentPreferencesRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentPreferences create(StudentPreferences studentPreferences) {
		StudentPreferences studentPreferencesEntity = null;
		Optional<StudentPreferences> existingStudentPreferences = repository
				.findById(studentPreferences.getStudentId());
		if (existingStudentPreferences.isPresent()) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		try {
			studentPreferencesEntity = repository.save(studentPreferences);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentPreferencesEntity;

	}

	public StudentPreferences update(StudentPreferences studentPreferences) {
		Long studentId = studentPreferences.getStudentId();
		StudentPreferences existingEntity = get(studentId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public StudentPreferences read(StudentPreferences studentPreferencesEntity) {
		Long studentId = studentPreferencesEntity.getStudentId();
		StudentPreferences studentPreferences = get(studentId);
		return studentPreferences;
	}

	public StudentPreferences close(StudentPreferences studentPreferencesEntity) {
		Long studentId = studentPreferencesEntity.getStudentId();
		StudentPreferences existingStudentPreferences = get(studentId);
		if (existingStudentPreferences.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentPreferences.setRecordInUse(RecordInUseType.N);
		existingStudentPreferences.setOperationDateTime(null);
		return repository.save(existingStudentPreferences);
	}

	public StudentPreferences reInStudentPreferences(StudentPreferences studentPreferencesEntity) {
		Long studentId = studentPreferencesEntity.getStudentId();
		StudentPreferences existingStudentPreferences = get(studentId);
		if (existingStudentPreferences.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentPreferences.setRecordInUse(RecordInUseType.Y);
		existingStudentPreferences.setOperationDateTime(null);
		return repository.save(existingStudentPreferences);
	}

	public Page<StudentPreferences> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentPreferences> studentPreferencesList = repositoryCustom.findAll(pageable, searchRequest);
		return studentPreferencesList;
	}
}