package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.repository.StudentPaymentRepository;

@Service
public class StudentPaymentService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	StudentPaymentRepository repository;
	
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
		Long paymentId = StudentPayment.getPaymentID();
		
		StudentPayment existingEntity = get(paymentId);
		modelMapper.map(StudentPayment, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentPayment patch(StudentPayment StudentPayment) {
		Long paymentId = StudentPayment.getPaymentID();

		StudentPayment existingEntity = get(paymentId);
		modelMapperForPatch.map(StudentPayment, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentPayment get(Long paymentId) {
		Optional<StudentPayment> existingEntity = repository.findById(paymentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
	}
	
	public String getAudit(Long paymentId) {
		StudentPayment StudentDetails = new StudentPayment();
		StudentDetails.setPaymentID(paymentId);
		return auditService.getEntityAudit(StudentDetails);
	}
	
}
