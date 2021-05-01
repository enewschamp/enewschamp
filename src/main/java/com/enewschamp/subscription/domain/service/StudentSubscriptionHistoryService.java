package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;

@Service
public class StudentSubscriptionHistoryService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentSubscriptionHistoryRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSubscriptionHistory create(StudentSubscriptionHistory studentSubscription) {
		return repository.save(studentSubscription);

	}

	public StudentSubscriptionHistory update(StudentSubscriptionHistory studentSubscription) {
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();
		StudentSubscriptionHistory existingEntity = get(studentId, editionId);
		modelMapper.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscriptionHistory patch(StudentSubscriptionHistory studentSubscription) {
		Long studentId = studentSubscription.getStudentId();
		String editionId = studentSubscription.getEditionId();

		StudentSubscriptionHistory existingEntity = get(studentId, editionId);
		modelMapperForPatch.map(studentSubscription, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscriptionHistory get(Long studentId, String editionId) {
		Optional<StudentSubscriptionHistory> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public List<StudentSubscriptionHistory> getAllByStudentIdAndEdition(Long studentId, String editionId) {
		return repository.getAllByStudentIdAndEdition(studentId, editionId);
	}

	public String getAudit(Long studentId) {
		StudentSubscriptionHistory studentDetails = new StudentSubscriptionHistory();
		studentDetails.setStudentId(studentId);
		return auditService.getEntityAudit(studentDetails);
	}

}
