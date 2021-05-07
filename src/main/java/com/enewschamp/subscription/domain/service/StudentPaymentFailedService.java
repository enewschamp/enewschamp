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
import com.enewschamp.app.admin.student.payment.failed.repository.StudentPaymentFailedRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentPaymentFailed;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.repository.StudentPaymentFailedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentPaymentFailedService {
	private final StudentPaymentFailedRepository repository;
	private final StudentPaymentFailedRepositoryCustomImpl repositoryCustom;
	private final AuditService auditService;
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;



	public StudentPaymentFailed create(StudentPaymentFailed studentPaymentFailed) {
		return repository.save(studentPaymentFailed);

	}

	public StudentPaymentFailed get(Long paymentId) {
		Optional<StudentPaymentFailed> existingEntity = repository.findById(paymentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public StudentPaymentFailed getByOrderId(String orderId) {
		Optional<StudentPaymentFailed> existingEntity = repository.getByOrderId(orderId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public List<StudentPaymentFailed> getAllByStudentIdAndEdition(Long studentId, String editionId) {
		return repository.getAllByStudentIdAndEdition(studentId, editionId);
	}

	public String getAudit(Long paymentId) {
		StudentPaymentWork StudentDetails = new StudentPaymentWork();
		StudentDetails.setPaymentId(paymentId);
		return auditService.getEntityAudit(StudentDetails);
	}

	public Page<StudentPaymentFailed> listStudentPaymentFailed(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentPaymentFailed> studentPaymentList = repositoryCustom.findAll(pageable, searchRequest);
		return studentPaymentList;
	}
}
