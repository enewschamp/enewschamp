package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPreferenceWork;
import com.enewschamp.subscription.domain.repository.StudentPreferenceWorkRepository;

@Service
public class StudentPreferenceWorkService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPreferenceWorkRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	
	public StudentPreferenceWork create(StudentPreferenceWork StudentPreferenceWork) {
		return repository.save(StudentPreferenceWork);

	}

	
	public StudentPreferenceWork update(StudentPreferenceWork StudentPreferenceWork) {
		Long studentID = StudentPreferenceWork.getStudentID();
		
		StudentPreferenceWork existingEntity = get(studentID);
		modelMapper.map(StudentPreferenceWork, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentPreferenceWork patch(StudentPreferenceWork StudentPreferenceWork) {
		Long studentID = StudentPreferenceWork.getStudentID();

		StudentPreferenceWork existingEntity = get(studentID);
		modelMapperForPatch.map(StudentPreferenceWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPreferenceWork get(Long studentID) {
		Optional<StudentPreferenceWork> existingEntity = repository.findById(studentID);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
	}
	
	public String getAudit(Long studentID) {
		StudentPreferenceWork StudentDetails = new StudentPreferenceWork();
		StudentDetails.setStudentID(studentID);
		return auditService.getEntityAudit(StudentDetails);
	}
}
