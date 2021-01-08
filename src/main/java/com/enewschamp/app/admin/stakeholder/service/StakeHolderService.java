package com.enewschamp.app.admin.stakeholder.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.stakeholder.entity.StakeHolder;
import com.enewschamp.app.admin.stakeholder.repository.StakeHolderRepository;
import com.enewschamp.app.admin.stakeholder.repository.StakeHolderRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StakeHolderService {

	@Autowired
	private StakeHolderRepository repository;

	@Autowired
	private StakeHolderRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public StakeHolder create(StakeHolder stakeHolderEntity) {
		return repository.save(stakeHolderEntity);
	}

	public StakeHolder update(StakeHolder stakeHolderEntity) {
		Long stakeHolderId = stakeHolderEntity.getStakeHolderId();
		StakeHolder existingStakeHolder = get(stakeHolderId);
		modelMapper.map(stakeHolderEntity, existingStakeHolder);
		return repository.save(existingStakeHolder);
	}

	public StakeHolder get(Long StakeHolderId) {
		Optional<StakeHolder> existingEntity = repository.findById(StakeHolderId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_NOT_FOUND);
		}
	}

	public StakeHolder read(StakeHolder stakeHolderEntity) {
		Long stakeHolderId = stakeHolderEntity.getStakeHolderId();
		StakeHolder stakeHolder = get(stakeHolderId);
		return stakeHolder;
	}

	public StakeHolder close(StakeHolder stakeHolderEntity) {
		Long stakeHolderId = stakeHolderEntity.getStakeHolderId();
		StakeHolder existingStakeHolder = get(stakeHolderId);
		if (existingStakeHolder.getRecordInUse().equals(RecordInUseType.N)) {
			return existingStakeHolder;
		}
		existingStakeHolder.setRecordInUse(RecordInUseType.N);
		existingStakeHolder.setOperationDateTime(null);
		return repository.save(existingStakeHolder);
	}

	public StakeHolder reInstate(StakeHolder stakeHolderEntity) {
		Long stakeHolderId = stakeHolderEntity.getStakeHolderId();
		StakeHolder existingStakeHolder = get(stakeHolderId);
		if (existingStakeHolder.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingStakeHolder;
		}
		existingStakeHolder.setRecordInUse(RecordInUseType.Y);
		existingStakeHolder.setOperationDateTime(null);
		return repository.save(existingStakeHolder);
	}

	public Page<StakeHolder> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StakeHolder> stakeHolderList = repositoryCustom.findStakeHolders(pageable, searchRequest);
		return stakeHolderList;
	}

}
