package com.enewschamp.problem;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.enewschamp.publication.domain.service.ValidationError;

@Immutable
public final class BusinessException extends RuntimeException {


	private static final long serialVersionUID = 2017344958429707915L;
	
	private final String errorCode;

	private List<ValidationError> validationErrors = null;
	
	private String[] errorMessageParams;

	public BusinessException(final String errorCode, String... errorMessageParams) {
		this.errorCode = errorCode;
		this.errorMessageParams = errorMessageParams;
	}
	
	public BusinessException(final String errorCode, List<ValidationError> validationErrors) {
		this.errorCode = errorCode;
		this.validationErrors = validationErrors;
		this.errorMessageParams = null;
	}
	
	public BusinessException(final String errorCode, List<ValidationError> validationErrors, String... errorMessageParams) {
		this.errorCode = errorCode;
		this.validationErrors = validationErrors;
		this.errorMessageParams = errorMessageParams;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public List<ValidationError> getValidationErrors() {
		return this.validationErrors;
	}

	public String[] getErrorMessageParams() {
		return this.errorMessageParams;
	}
}
