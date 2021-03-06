package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@Service
public class StudentSubscriptionWorkService {

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

	public void delete(Long studentId) {
		Optional<StudentSubscriptionWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			repository.deleteById(studentId);
		}
	}

	public StudentSubscriptionWork update(StudentSubscriptionWork StudentSubscriptionWork) {
		Long studentId = StudentSubscriptionWork.getStudentId();
		String editionId = StudentSubscriptionWork.getEditionId();

		StudentSubscriptionWork existingEntity = get(studentId, editionId);
		modelMapper.map(StudentSubscriptionWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscriptionWork patch(StudentSubscriptionWork StudentSubscriptionWork) {
		Long studentId = StudentSubscriptionWork.getStudentId();
		String editionId = StudentSubscriptionWork.getEditionId();

		StudentSubscriptionWork existingEntity = get(studentId, editionId);
		modelMapperForPatch.map(StudentSubscriptionWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSubscriptionWork get(Long studentId, String editionId) {
		Optional<StudentSubscriptionWork> existingEntity = repository.getStudentByIdAndEdition(studentId, editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			// throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentSubscriptionWork StudentDetails = new StudentSubscriptionWork();
		StudentDetails.setStudentId(studentId);
		return auditService.getEntityAudit(StudentDetails);
	}

}