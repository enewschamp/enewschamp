package com.enewschamp.app.admin.entitlement.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.entitlement.repository.Entitlement;
import com.enewschamp.app.admin.entitlement.repository.EntitlementRepository;
import com.enewschamp.app.admin.entitlement.repository.EntitlementRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class EntitlementService {

	@Autowired
	private EntitlementRepository repository;

	@Autowired
	private EntitlementRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Entitlement create(Entitlement entitlementEntity) {
		return repository.save(entitlementEntity);
	}

	public Entitlement update(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntitlement = get(entitlementId);
		modelMapper.map(entitlementEntity, existingEntitlement);
		return repository.save(existingEntitlement);
	}

	public Entitlement get(Long entitlementId) {
		Optional<Entitlement> existingEntity = repository.findById(entitlementId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.HOLIDAY_NOT_FOUND);
		}
	}

	public Entitlement read(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntitlement = get(entitlementId);
		existingEntitlement.setRecordInUse(RecordInUseType.Y);
		return repository.save(existingEntitlement);
	}

	public Entitlement close(Entitlement entitlementEntity) {
		Long entitlementId = entitlementEntity.getEntitlementId();
		Entitlement existingEntity = get(entitlementId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return repository.save(existingEntity);
	}

	public Page<Entitlement> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Entitlement> entitlementList = repositoryCustom.findEntitlements(pageable);
		return entitlementList;
	}
}
