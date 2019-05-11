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
import com.enewschamp.subscription.domin.entity.StudentSubscriptionWork;

public class StudentSubscriptionWorkService  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentSubscriptionWorkRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	
	public StudentSubscriptionWork create(StudentSubscriptionWork StudentSubscriptionWork) {
		return repository.save(StudentSubscriptionWork);

	}

	
	public StudentSubscriptionWork update(StudentSubscriptionWork StudentSubscriptionWork) {
		Long studentId = StudentSubscriptionWork.getStudentID();
		Long editionId = StudentSubscriptionWork.getEditionID();
		
		StudentSubscriptionWork existingEntity = get(studentId,editionId);
		modelMapper.map(StudentSubscriptionWork, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentSubscriptionWork patch(StudentSubscriptionWork StudentSubscriptionWork) {
		Long studentId = StudentSubscriptionWork.getStudentID();
		Long editionId = StudentSubscriptionWork.getEditionID();

		StudentSubscriptionWork existingEntity = get(studentId,editionId);
		modelMapperForPatch.map(StudentSubscriptionWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscriptionWork get(Long studentId, Long editionId) {
		Optional<StudentSubscriptionWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentId) {
		StudentSubscriptionWork StudentDetails = new StudentSubscriptionWork();
		StudentDetails.setStudentID(studentId);
		return auditService.getEntityAudit(StudentDetails);
	}
	

}
