package com.enewschamp.app.admin.schoolchain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;
import com.enewschamp.app.admin.schoolchain.repository.SchoolChainRepository;
import com.enewschamp.app.admin.schoolchain.repository.SchoolChainRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class SchoolChainService {
	
	@Autowired
	private SchoolChainRepository repository;

	@Autowired
	private SchoolChainRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public SchoolChain create(SchoolChain schoolChainEntity) {
		SchoolChain schoolChain = null;
		try {
			schoolChain = repository.save(schoolChainEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return schoolChain;
	}

	public SchoolChain update(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain existingSchoolChain = get(schoolChainId);
		modelMapper.map(schoolChainEntity, existingSchoolChain);
		return repository.save(existingSchoolChain);
	}

	public SchoolChain get(Long promotionId) {
		Optional<SchoolChain> existingEntity = repository.findById(promotionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.HOLIDAY_NOT_FOUND);
		}
	}

	public SchoolChain read(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain existingSchoolChain = get(schoolChainId);
		if (existingSchoolChain.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingSchoolChain;
		}
		existingSchoolChain.setRecordInUse(RecordInUseType.Y);
		existingSchoolChain.setOperationDateTime(null);
		return repository.save(existingSchoolChain);
	}

	public SchoolChain close(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain existingEntity = get(schoolChainId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			return existingEntity;
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public SchoolChain reinstate(SchoolChain schoolChain) {
		Long schoolChainId = schoolChain.getSchoolChainId();
		SchoolChain existingSchoolChain = get(schoolChainId);
		if (existingSchoolChain.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingSchoolChain;
		}
		existingSchoolChain.setRecordInUse(RecordInUseType.Y);
		existingSchoolChain.setOperationDateTime(null);
		return repository.save(existingSchoolChain);
	}

	public Page<SchoolChain> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<SchoolChain> schoolChainList = repositoryCustom.findSchoolChains(pageable);
		return schoolChainList;
	}

}
