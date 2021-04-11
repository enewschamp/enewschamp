package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.payment.repository.StudentPaymentRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
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
	private StudentPaymentRepositoryCustomImpl repositoryCustom;

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
		List<StudentPayment> existingEntities = repository.getAllByStudentIdAndEdition(studentId, editionId);
		if (existingEntities != null && existingEntities.size() > 0) {
			return existingEntities.get(0);
		} else {
			return null;
		}
	}

	public List<StudentPayment> getAllByStudentIdAndEdition(Long studentId, String editionId) {
		return repository.getAllByStudentIdAndEdition(studentId, editionId);
	}

	public StudentPayment getByOrderId(String orderId) {
		Optional<StudentPayment> existingEntity = repository.getByOrderId(orderId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long paymentId) {
		StudentPayment StudentDetails = new StudentPayment();
		StudentDetails.setPaymentId(paymentId);
		return auditService.getEntityAudit(StudentDetails);
	}
	
	public Page<StudentPayment> listStudentPayment(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentPayment> userActivityTrackerList = repositoryCustom.findAll(pageable, searchRequest);
		if (userActivityTrackerList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return userActivityTrackerList;
	}

}