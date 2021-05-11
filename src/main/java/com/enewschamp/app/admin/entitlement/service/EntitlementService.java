package com.enewschamp.app.admin.entitlement.service;

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
import com.enewschamp.app.admin.entitlement.repository.Entitlement;
import com.enewschamp.app.admin.entitlement.repository.EntitlementRepository;
import com.enewschamp.app.admin.entitlement.repository.EntitlementRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class EntitlementService {

	@Autowired
	private EntitlementRepository repository;

	@Autowired
	private EntitlementRepositoryCustomImpl repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Entitlement create(Entitlement entitlementEntity) {
		Entitlement entitlement = null;
		try {
			entitlement = repository.save(entitlementEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return entitlement;
	}

	public Entitlement update(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntitlement = get(entitlementId);
		if (existingEntitlement.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(entitlementEntity, existingEntitlement);
		return repository.save(existingEntitlement);
	}

	public Entitlement get(Long entitlementId) {
		Optional<Entitlement> existingEntity = repository.findById(entitlementId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.ENTITLEMENT_NOT_FOUND);
		}
	}

	public Entitlement read(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntitlement = get(entitlementId);
		return existingEntitlement;
	}

	public Entitlement close(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntity = get(entitlementId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Entitlement reinstate(Entitlement entitlement) {
		Long entitlementId = entitlement.getEntitlementId();
		Entitlement existingEntity = get(entitlementId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingEntity.setRecordInUse(RecordInUseType.Y);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Page<Entitlement> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Entitlement> entitlementList = repositoryCustom.findAll(pageable, searchRequest);
		return entitlementList;
	}

	public Boolean isValidUser(String userId, String role, String pageName, String action) {
		Optional<Entitlement> entitlement = repository.findByUserIdAndRoleAndPageNameAndAction(userId, role, pageName, action);
		if (entitlement.isPresent())
			return true;
		else
			throw new BusinessException(ErrorCodeConstants.ENTITLEMENT_NOT_FOUND);

	}
}
