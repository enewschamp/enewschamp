package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.domin.entity.StudentSubscription;

@Service
public class StudentSubscriptionService  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentSubscriptionRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	
	public StudentSubscription create(StudentSubscription studentSubscription) {
		return repository.save(studentSubscription);

	}

	
	public StudentSubscription update(StudentSubscription studentSubscription) {
		Long studentId = studentSubscription.getStudentID();
		Long editionId = studentSubscription.getEditionID();
		
		StudentSubscription existingEntity = get(studentId,editionId);
		modelMapper.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentSubscription patch(StudentSubscription studentSubscription) {
		Long studentId = studentSubscription.getStudentID();
		Long editionId = studentSubscription.getEditionID();

		StudentSubscription existingEntity = get(studentId,editionId);
		modelMapperForPatch.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscription get(Long studentId, Long editionId) {
		Optional<StudentSubscription> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentId) {
		StudentSubscription StudentDetails = new StudentSubscription();
		StudentDetails.setStudentID(studentId);
		return auditService.getEntityAudit(StudentDetails);
	}
	

}
