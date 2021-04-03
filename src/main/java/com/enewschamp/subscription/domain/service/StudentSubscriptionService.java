package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.user.domain.entity.StudentRefund;

@Service
public class StudentSubscriptionService {

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
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();

		StudentSubscription existingEntity = get(studentId, editionId);
		modelMapper.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscription patch(StudentSubscription studentSubscription) {
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();

		StudentSubscription existingEntity = get(studentId, editionId);
		modelMapperForPatch.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscription get(Long studentId, String editionId) {
		Optional<StudentSubscription> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public List<StudentSubscription> getSubscriptionRenewalList() {
		return repository.getSubscriptionRenewalList();
	}

	public String getAudit(Long studentId) {
		StudentSubscription StudentDetails = new StudentSubscription();
		StudentDetails.setStudentId(studentId);
		return auditService.getEntityAudit(StudentDetails);
	}

}
