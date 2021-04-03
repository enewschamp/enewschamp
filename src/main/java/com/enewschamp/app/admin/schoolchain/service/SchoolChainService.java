package com.enewschamp.app.admin.schoolchain.service;

import java.sql.SQLIntegrityConstraintViolationException;
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
import com.enewschamp.app.admin.schoolchain.repository.SchoolChainRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class SchoolChainService {

	@Autowired
	private SchoolChainRepository repository;

	@Autowired
	private SchoolChainRepositoryCustomImpl repositoryCustom;

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
			Throwable thr = e.getCause().getCause();
			if (thr instanceof SQLIntegrityConstraintViolationException) {
				throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
			} else
				throw e;
		}
		return schoolChain;
	}

	public SchoolChain update(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain existingSchoolChain = get(schoolChainId);
		if (existingSchoolChain.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(schoolChainEntity, existingSchoolChain);
		return repository.save(existingSchoolChain);
	}

	public SchoolChain get(Long promotionId) {
		Optional<SchoolChain> existingEntity = repository.findById(promotionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_CHAIN_NOT_FOUND);
		}
	}

	public SchoolChain read(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain schoolChain = get(schoolChainId);
		return schoolChain;

	}

	public SchoolChain close(SchoolChain schoolChainEntity) {
		Long schoolChainId = schoolChainEntity.getSchoolChainId();
		SchoolChain existingEntity = get(schoolChainId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public SchoolChain reinstate(SchoolChain schoolChain) {
		Long schoolChainId = schoolChain.getSchoolChainId();
		SchoolChain existingSchoolChain = get(schoolChainId);
		if (existingSchoolChain.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingSchoolChain.setRecordInUse(RecordInUseType.Y);
		existingSchoolChain.setOperationDateTime(null);
		return repository.save(existingSchoolChain);
	}

	public Page<SchoolChain> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<SchoolChain> schoolChainList = repositoryCustom.findAll(pageable, null);
		if (schoolChainList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return schoolChainList;
	}

}
