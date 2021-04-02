package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.repository.StudentPaymentRepository;
import com.enewschamp.subscription.domain.repository.StudentPaymentWorkRepository;

@Service
public class StudentPaymentService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPaymentRepository repository;

	@Autowired
	StudentPaymentWorkRepository workRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentPayment create(StudentPayment StudentPayment) {
		return repository.save(StudentPayment);

	}

	public StudentPayment update(StudentPayment StudentPayment) {
		Long paymentId = StudentPayment.getPaymentId();

		StudentPayment existingEntity = get(paymentId);
		modelMapper.map(StudentPayment, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPayment patch(StudentPayment StudentPayment) {
		Long paymentId = StudentPayment.getPaymentId();

		StudentPayment existingEntity = get(paymentId);
		modelMapperForPatch.map(StudentPayment, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPayment get(Long paymentId) {
		Optional<StudentPayment> existingEntity = repository.findById(paymentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public StudentPayment getByStudentIdAndEdition(Long studentId, String editionId) {
		List<StudentPayment> existingEntities = repository.getByStudentIdAndEdition(studentId, editionId);
		if (existingEntities != null && existingEntities.size() > 0) {
			return existingEntities.get(0);
		} else {
			return null;
		}
	}

	public Long getStudentByOrderIdAndTxnId(String orderId, String paytmTxnId) {
		List<StudentPayment> existingEntities = repository.getByOrderIdAndTxnId(orderId, paytmTxnId);
		if (existingEntities != null && existingEntities.size() > 0) {
			return existingEntities.get(0).getStudentId();
		} else {
			List<StudentPaymentWork> existingEntitiesWork = workRepository.getByOrderIdAndTxnId(orderId, paytmTxnId);
			if (existingEntitiesWork != null && existingEntitiesWork.size() > 0) {
				return existingEntitiesWork.get(0).getStudentId();
			} else {
				return null;
			}
		}
	}

	public String getAudit(Long paymentId) {
		StudentPayment StudentDetails = new StudentPayment();
		StudentDetails.setPaymentId(paymentId);
		return auditService.getEntityAudit(StudentDetails);
	}

}
