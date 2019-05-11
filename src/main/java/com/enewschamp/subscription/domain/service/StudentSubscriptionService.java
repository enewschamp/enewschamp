package com.enewschamp.subscription.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.subscription.domin.entity.StudentSubscription;

public class StudentSubscriptionService implements IStudentSubscription {

	@Autowired
	StudentSubscriptionRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	@Override
	public StudentSubscription create(StudentSubscription studentSubscription) {
		return repository.save(studentSubscription);

	}

	@Override
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

	@Override
	public void delete(Long studenId, Long editionId) {

		
	}

	@Override
	public StudentSubscription get(Long studenId, Long editionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAudit(Long studenId, Long editionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
