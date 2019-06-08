package com.enewschamp.publication.domain.service;

import lombok.Data;

@Data
public class ValidationError {
	
	private String errorCode;
	
	private String fieldName;
	
	private String errorMessage;
	
	private String[] errorMessageParams;

	public ValidationError(String errorCode, String fieldName) {
		this.errorCode = errorCode;
		this.fieldName = fieldName;
	}
	
	public ValidationError(String errorCode, String fieldName, String... errorMessageParams) {
		this.errorCode = errorCode;
		this.fieldName = fieldName;
		this.errorMessageParams = errorMessageParams;
	}
		
}
