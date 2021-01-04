package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.repository.StudentPaymentWorkRepository;

@Service
public class StudentPaymentWorkService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPaymentWorkRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentPaymentWork create(StudentPaymentWork StudentPaymentWork) {
		return repository.save(StudentPaymentWork);

	}

	public void delete(Long paymentId) {
		repository.deleteById(paymentId);
	}

	public void deleteByStudentId(Long studentId) {
		repository.deleteByStudentId(studentId);
	}

	public StudentPaymentWork update(StudentPaymentWork StudentPaymentWork) {
		Long paymentId = StudentPaymentWork.getPaymentId();

		StudentPaymentWork existingEntity = get(paymentId);
		modelMapper.map(StudentPaymentWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPaymentWork patch(StudentPaymentWork StudentPaymentWork) {
		Long paymentId = StudentPaymentWork.getPaymentId();
		StudentPaymentWork existingEntity = get(paymentId);
		modelMapperForPatch.map(StudentPaymentWork, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPaymentWork get(Long paymentId) {
		Optional<StudentPaymentWork> existingEntity = repository.findById(paymentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public StudentPaymentWork getByStudentIdAndEdition(Long studentId, String editionId) {
		Optional<StudentPaymentWork> existingEntity = repository.getByStudentIdAndEdition(studentId, editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long paymentId) {
		StudentPaymentWork StudentDetails = new StudentPaymentWork();
		StudentDetails.setPaymentId(paymentId);
		return auditService.getEntityAudit(StudentDetails);
	}

}
