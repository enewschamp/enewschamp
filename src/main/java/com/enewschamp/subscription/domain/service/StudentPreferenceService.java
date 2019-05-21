package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.domain.entity.StudentPreference;
import com.enewschamp.subscription.domain.repository.StudentPreferenceRepository;

public class StudentPreferenceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPreferenceRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	
	public StudentPreference create(StudentPreference StudentPreference) {
		return repository.save(StudentPreference);

	}

	
	public StudentPreference update(StudentPreference StudentPreference) {
		Long studentID = StudentPreference.getStudentID();
		
		StudentPreference existingEntity = get(studentID);
		modelMapper.map(StudentPreference, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentPreference patch(StudentPreference StudentPreference) {
		Long studentID = StudentPreference.getStudentID();

		StudentPreference existingEntity = get(studentID);
		modelMapperForPatch.map(StudentPreference, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPreference get(Long studentID) {
		Optional<StudentPreference> existingEntity = repository.findById(studentID);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentID) {
		StudentPreference StudentDetails = new StudentPreference();
		StudentDetails.setStudentID(studentID);
		return auditService.getEntityAudit(StudentDetails);
	}
}
