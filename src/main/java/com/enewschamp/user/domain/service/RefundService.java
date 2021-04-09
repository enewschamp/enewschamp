package com.enewschamp.user.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.refund.repository.StudentRefundRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.StudentRefund;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefundService extends AbstractDomainService {

	@Autowired
	RefundRepository repository;

	@Autowired
	StudentRefundRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	PropertiesBackendService propertiesService;

	public StudentRefund create(StudentRefund refund) {
		StudentRefund refundEntity = null;
		try {
			refundEntity = repository.save(refund);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}

		return refundEntity;
	}

	public StudentRefund update(StudentRefund refund) {
		StudentRefund existingRefund = load(refund.getRefundId());
		if (existingRefund.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(refund, existingRefund);
		return repository.save(existingRefund);
	}

	public StudentRefund patch(StudentRefund refund) {
		StudentRefund existingEntity = load(refund.getRefundId());
		modelMapperForPatch.map(refund, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long refundId) {
		repository.deleteById(refundId);
	}

	public StudentRefund load(Long refundId) {
		Optional<StudentRefund> existingEntity = repository.findById(refundId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND, "" + refundId);
		}
	}

	public StudentRefund get(Long refundId) {
		if (refundId == null || refundId == 0L) {
			return null;
		}
		Optional<StudentRefund> existingEntity = repository.findById(refundId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}

	public StudentRefund read(StudentRefund studentRefundEntity) {
		Long refundId = studentRefundEntity.getRefundId();
		StudentRefund studentRefund = get(refundId);
		return studentRefund;

	}

	public StudentRefund close(StudentRefund studentRefundEntity) {
		Long studentRefundId = studentRefundEntity.getRefundId();
		StudentRefund existingStudentRefund = get(studentRefundId);
		if (existingStudentRefund.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentRefund.setRecordInUse(RecordInUseType.N);
		existingStudentRefund.setOperationDateTime(null);
		return repository.save(existingStudentRefund);
	}

	public StudentRefund reinstate(StudentRefund studentRefundEntity) {
		Long studentRefundId = studentRefundEntity.getRefundId();
		StudentRefund existingStudentRefund = get(studentRefundId);
		if (existingStudentRefund.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentRefund.setRecordInUse(RecordInUseType.Y);
		existingStudentRefund.setOperationDateTime(null);
		return repository.save(existingStudentRefund);
	}

	public Page<StudentRefund> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentRefund> StudentRefundList = customRepository.findAll(pageable, searchRequest);
		if (StudentRefundList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return StudentRefundList;
	}
}
