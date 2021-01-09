package com.enewschamp.app.admin.institution.service;

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
import com.enewschamp.app.admin.institution.entity.InstitutionAddress;
import com.enewschamp.app.admin.institution.repository.InstitutionAddressRepository;
import com.enewschamp.app.admin.institution.repository.InstitutionAddressRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class InstitutionAddressService {
	@Autowired
	private InstitutionAddressRepository repository;

	@Autowired
	private InstitutionAddressRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public InstitutionAddress create(InstitutionAddress institutionAddressEntity) {
		InstitutionAddress institutionAddress = null;
		try {
			institutionAddress = repository.save(institutionAddressEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return institutionAddress;
	}

	public InstitutionAddress update(InstitutionAddress institutionAddressEntity) {
		Long institutionAddressId = institutionAddressEntity.getInstitutionId();
		InstitutionAddress existingInstitution = get(institutionAddressId);
		modelMapper.map(institutionAddressEntity, existingInstitution);
		return repository.save(existingInstitution);
	}

	public InstitutionAddress get(Long institutionAddressId) {
		Optional<InstitutionAddress> existingEntity = repository.findById(institutionAddressId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.HOLIDAY_NOT_FOUND);
		}
	}

	public InstitutionAddress read(InstitutionAddress institutionAddressEntity) {
		Long institutionAddressId = institutionAddressEntity.getInstitutionId();
		InstitutionAddress institutionAddress = get(institutionAddressId);
        return institutionAddress;
	}

	public InstitutionAddress close(InstitutionAddress institutionAddressEntity) {
		Long institutionAddressId = institutionAddressEntity.getInstitutionId();
		InstitutionAddress existingEntity = get(institutionAddressId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			return existingEntity;
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public InstitutionAddress reinstate(InstitutionAddress institutionAddress) {
		Long institutionAddressId = institutionAddress.getInstitutionId();
		InstitutionAddress existingInstitutionAddress = get(institutionAddressId);
		if (existingInstitutionAddress.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingInstitutionAddress;
		}
		existingInstitutionAddress.setRecordInUse(RecordInUseType.Y);
		existingInstitutionAddress.setOperationDateTime(null);
		return repository.save(existingInstitutionAddress);
	}

	public Page<InstitutionAddress> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<InstitutionAddress> institutionList = repositoryCustom.findInstitutionAddresses(pageable, searchRequest);
		return institutionList;
	}
}