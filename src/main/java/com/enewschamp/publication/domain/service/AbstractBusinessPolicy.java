package com.enewschamp.publication.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.problem.BusinessException;

public abstract class AbstractBusinessPolicy {

	private List<ValidationError> validationErrors;

	protected String policyCode;

	public void validate() {
		validatePolicy();
	}

	public void validateAndThrow() {
		validatePolicy();
		if (validationErrors != null && validationErrors.size() > 0) {
			if (validationErrors.get(0).getErrorCode() != null) {
				throw new BusinessException(validationErrors.get(0).getErrorCode(), validationErrors);
			} else {
				throw new BusinessException(ErrorCodeConstants.BUS_POLICY_FAILED, validationErrors);
			}
		}
	}

	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	protected void addValidationError(ValidationError validationError) {
		if (validationErrors == null) {
			validationErrors = new ArrayList<ValidationError>();
		}
		validationErrors.add(validationError);

	}

	protected abstract void validatePolicy();

}
