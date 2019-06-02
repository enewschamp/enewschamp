package com.enewschamp.publication.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;

public abstract class AbstractBusinessPolicy {
	
	private List<ValidationError> validationErrors;
	
	protected String policyCode;
	
	public void validate() {
		validatePolicy();
	}
	
	public void validateAndThrow() {
		validatePolicy();
		if(validationErrors != null && validationErrors.size() > 0) {
			throw new BusinessException(ErrorCodes.BUS_POLICY_FAILED, validationErrors);
		}
	}
	
	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}
	
	protected void addValidationError(ValidationError validationError) {
		if(validationErrors == null) {
			validationErrors = new ArrayList<ValidationError>();
		}
		validationErrors.add(validationError);
		
	}
	
	protected abstract void validatePolicy();
		
}
