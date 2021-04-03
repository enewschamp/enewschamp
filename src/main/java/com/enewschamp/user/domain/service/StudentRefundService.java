package com.enewschamp.user.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.StudentRefund;

@Service
public class StudentRefundService extends AbstractDomainService {

	@Autowired
	StudentRefundRepository repository;

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
		return repository.save(refund);
	}

	public StudentRefund update(StudentRefund refund) {
		StudentRefund existingRefund = load(refund.getRefundId());
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

	public List<StudentRefund> getPendingRefundList() {
		return repository.getPendingRefundList();
	}
}
