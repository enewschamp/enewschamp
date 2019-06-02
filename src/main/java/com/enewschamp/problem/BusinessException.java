package com.enewschamp.problem;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.enewschamp.publication.domain.service.ValidationError;

@Immutable
public final class BusinessException extends RuntimeException {


	private static final long serialVersionUID = 2017344958429707915L;
	
	private final String errorCode;

	private List<ValidationError> validationErrors = null;

	public BusinessException(final String errorCode) {
		this.errorCode = errorCode;
	}
	
	public BusinessException(final String errorCode, List<ValidationError> validationErrors) {
		this.errorCode = errorCode;
		this.validationErrors = validationErrors;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public List<ValidationError> getValidationErrors() {
		return this.validationErrors;
	}

}
