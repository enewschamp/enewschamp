package com.enewschamp.app.admin.institutionstakeholder.service;

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
import com.enewschamp.app.admin.institutionstakeholder.entity.InstitutionStakeholder;
import com.enewschamp.app.admin.institutionstakeholder.repository.InstitutionStakeholderRepository;
import com.enewschamp.app.admin.institutionstakeholder.repository.InstitutionStakeholderRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class InstitutionStakeholderService {

	@Autowired
	private InstitutionStakeholderRepository repository;

	@Autowired
	private InstitutionStakeholderRepositoryCustomImpl repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public InstitutionStakeholder create(InstitutionStakeholder instStakeHolderEntity) {
		InstitutionStakeholder stakeHolder = null;
		try {
			stakeHolder = repository.save(instStakeHolderEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return stakeHolder;
	}

	public InstitutionStakeholder update(InstitutionStakeholder instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getStakeHolderId();
		InstitutionStakeholder existingInstitutionStakeholder = get(stakeHolderId);
		if (existingInstitutionStakeholder.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(instStakeHolderEntity, existingInstitutionStakeholder);
		return repository.save(existingInstitutionStakeholder);
	}

	public InstitutionStakeholder get(Long InstitutionStakeholderId) {
		Optional<InstitutionStakeholder> existingEntity = repository.findById(InstitutionStakeholderId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.INSTITUTIONAL_STAKE_HOLDER_NOT_FOUND);
		}
	}

	public InstitutionStakeholder read(InstitutionStakeholder instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getStakeHolderId();
		InstitutionStakeholder stakeHolder = get(stakeHolderId);
		return stakeHolder;
	}

	public InstitutionStakeholder close(InstitutionStakeholder instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getStakeHolderId();
		InstitutionStakeholder existingInstitutionStakeholder = get(stakeHolderId);
		if (existingInstitutionStakeholder.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingInstitutionStakeholder.setRecordInUse(RecordInUseType.N);
		existingInstitutionStakeholder.setOperationDateTime(null);
		return repository.save(existingInstitutionStakeholder);
	}

	public InstitutionStakeholder reInstate(InstitutionStakeholder instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getStakeHolderId();
		InstitutionStakeholder existingInstitutionStakeholder = get(stakeHolderId);
		if (existingInstitutionStakeholder.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingInstitutionStakeholder.setRecordInUse(RecordInUseType.Y);
		existingInstitutionStakeholder.setOperationDateTime(null);
		return repository.save(existingInstitutionStakeholder);
	}

	public Page<InstitutionStakeholder> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<InstitutionStakeholder> stakeHolderList = repositoryCustom.findAll(pageable, searchRequest);
		return stakeHolderList;
	}

}