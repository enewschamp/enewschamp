package com.enewschamp.common.domain.service;

import java.util.List;
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
import com.enewschamp.app.admin.errorcode.repository.ErrorCodesRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class ErrorCodesService extends AbstractDomainService {

	@Autowired
	ErrorCodesRepository repository;

	@Autowired
	ErrorCodesRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	public ErrorCodesService(ErrorCodesRepository repository) {
		this.repository = repository;
	}

	public String getValue(String errorCode) {
		ErrorCodes errorCodes = repository.getErrorCodes(errorCode);
		return errorCodes == null ? null : errorCodes.getErrorMessage();
	}

	public ErrorCodes create(ErrorCodes errorCodes) {
		ErrorCodes errorCodesEntity = null;
		try {
			errorCodesEntity = repository.save(errorCodes);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return errorCodesEntity;
	}

	public ErrorCodes update(ErrorCodes errorCodes) {
		Long errorCodeId = errorCodes.getErrorCodeId();
		ErrorCodes existingErrorCodes = get(errorCodeId);
		if (existingErrorCodes.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(errorCodes, existingErrorCodes);
		return repository.save(existingErrorCodes);
	}

	public ErrorCodes patch(ErrorCodes errorCodes) {
		Long errorCodeId = errorCodes.getErrorCodeId();
		ErrorCodes existingEntity = get(errorCodeId);
		modelMapperForPatch.map(errorCodes, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long errorCodeId) {
		repository.deleteById(errorCodeId);
	}

	public ErrorCodes get(Long errorCodeId) {
		Optional<ErrorCodes> existingEntity = repository.findById(errorCodeId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.ERROR_CODE_NOT_FOUND, String.valueOf(errorCodeId));
		}
	}

	public String getAudit(Long errorCodeId) {
		ErrorCodes errorCodes = new ErrorCodes();
		errorCodes.setErrorCodeId(errorCodeId);
		return auditService.getEntityAudit(errorCodes);
	}

	public ErrorCodes getEdition(Long errorCodeId) {
		Optional<ErrorCodes> existingEntity = repository.findById(errorCodeId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.ERROR_CODE_NOT_FOUND, String.valueOf(errorCodeId));
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getErrorCodesLOV());
	}

	public ErrorCodes read(ErrorCodes errorCodesEntity) {
		Long errorCodesId = errorCodesEntity.getErrorCodeId();
		ErrorCodes existingEntity = get(errorCodesId);
		return existingEntity;

	}

	public ErrorCodes close(ErrorCodes errorCodesEntity) {
		Long errorCodesId = errorCodesEntity.getErrorCodeId();
		ErrorCodes errorCodes = get(errorCodesId);
		if (errorCodes.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		errorCodes.setRecordInUse(RecordInUseType.N);
		errorCodes.setOperationDateTime(null);
		return repository.save(errorCodes);
	}

	public ErrorCodes reinstate(ErrorCodes errorCodesEntity) {
		Long errorCodesId = errorCodesEntity.getErrorCodeId();
		ErrorCodes errorCodes = get(errorCodesId);
		if (errorCodes.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		errorCodes.setRecordInUse(RecordInUseType.Y);
		errorCodes.setOperationDateTime(null);
		return repository.save(errorCodes);
	}

	public Page<ErrorCodes> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<ErrorCodes> errorCodesList = customRepository.findAll(pageable, searchRequest);
		if (errorCodesList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return errorCodesList;
	}
	
	public void createAll(List<ErrorCodes> errorCodes) {
		try {
			 repository.saveAll(errorCodes);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
	}

}
