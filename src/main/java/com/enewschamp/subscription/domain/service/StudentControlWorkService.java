package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.repository.StudentControlWorkRepository;

@Service
public class StudentControlWorkService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	StudentControlWorkRepository repository;

	public StudentControlWork create(StudentControlWork StudentControlWork) {
		return repository.save(StudentControlWork);
	}

	public StudentControlWork update(StudentControlWork StudentControlWork) {
		Long studentId = StudentControlWork.getStudentId();

		StudentControlWork existingEntity = get(studentId);
		modelMapper.map(StudentControlWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControlWork patch(StudentControlWork StudentControlWork) {
		Long studentId = StudentControlWork.getStudentId();

		StudentControlWork existingEntity = get(studentId);
		modelMapperForPatch.map(StudentControlWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControlWork get(Long studentId) {
		Optional<StudentControlWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public boolean studentExistByEmail(String emailId) {
		StudentControlWork existingEntity = repository.searchByEmail(emailId);
		if (existingEntity != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean studentExist(Long studentId) {
		return repository.existsById(studentId);

	}

	public void delete(Long studentId) {
		repository.deleteById(studentId);
	}

	public String getAudit(Long studentId) {
		StudentControlWork StudentControlWork = new StudentControlWork();
		StudentControlWork.setStudentId(studentId);
		return auditService.getEntityAudit(StudentControlWork);
	}

	public StudentControlWork getStudentByEmail(String emailId) {
		StudentControlWork existingEntity = repository.searchByEmail(emailId);
		return existingEntity;
	}

}