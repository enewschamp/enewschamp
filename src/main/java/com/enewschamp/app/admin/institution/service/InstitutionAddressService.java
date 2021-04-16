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
import com.enewschamp.app.admin.institution.repository.InstitutionAddressRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class InstitutionAddressService {
	@Autowired
	private InstitutionAddressRepository repository;

	@Autowired
	private InstitutionAddressRepositoryCustomImpl repositoryCustom;

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
		Long addressId = institutionAddressEntity.getAddressId();
		InstitutionAddress existingInstitution = get(addressId);
		if(existingInstitution.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(institutionAddressEntity, existingInstitution);
		return repository.save(existingInstitution);
	}

	public InstitutionAddress get(Long addressId) {
		Optional<InstitutionAddress> existingEntity = repository.findById(addressId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.INSTITUTION_ADDRESS_NOT_FOUND);
		}
	}

	public InstitutionAddress read(InstitutionAddress institutionAddressEntity) {
		Long addressId = institutionAddressEntity.getAddressId();
		InstitutionAddress institutionAddress = get(addressId);
        return institutionAddress;
	}

	public InstitutionAddress close(InstitutionAddress institutionAddressEntity) {
		Long addressId = institutionAddressEntity.getAddressId();
		InstitutionAddress existingEntity = get(addressId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public InstitutionAddress reinstate(InstitutionAddress institutionAddress) {
		Long addressId = institutionAddress.getAddressId();
		InstitutionAddress existingInstitutionAddress = get(addressId);
		if (existingInstitutionAddress.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingInstitutionAddress.setRecordInUse(RecordInUseType.Y);
		existingInstitutionAddress.setOperationDateTime(null);
		return repository.save(existingInstitutionAddress);
	}

	public Page<InstitutionAddress> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<InstitutionAddress> institutionList = repositoryCustom.findAll(pageable, searchRequest);
		return institutionList;
	}
}