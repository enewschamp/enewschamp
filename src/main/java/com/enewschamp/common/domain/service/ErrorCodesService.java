package com.enewschamp.common.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.entity.ErrorCodes;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class ErrorCodesService extends AbstractDomainService {

	@Autowired
	ErrorCodesRepository repository;

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
		return repository.save(errorCodes);
	}

	public ErrorCodes update(ErrorCodes errorCodes) {
		Long errorCodeId = errorCodes.getErrorCodeId();
		ErrorCodes existingErrorCodes = get(errorCodeId);
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
}
