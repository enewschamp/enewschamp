package com.enewschamp.problem;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.publication.domain.service.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Immutable
public final class Fault extends ThrowableProblem {

	private static final long serialVersionUID = -6897678664880320263L;

	@JsonIgnore
	private StatusType status;

	@JsonInclude
	private HeaderDTO header;

	@JsonInclude
	private ErrorPageData data;

	public Fault(final BusinessException e, final HeaderDTO header) {
		this(e.getErrorCode(), e.getErrorMessageParams(), e.getValidationErrors(), header);
	}

	public Fault(final String errorCode, final HeaderDTO header) {
		this(errorCode, null, null, header);
	}

	public Fault(final BusinessException e) {
		this(e.getErrorCode(), e.getErrorMessageParams(), e.getValidationErrors(), null);
	}

	Fault(final String errorCode, final String[] errorMessageParams, List<ValidationError> validationErrors,
			final HeaderDTO header) {
		ErrorPageData errorPageData = new ErrorPageData();
		errorPageData.setErrorCode(errorCode);
		errorPageData.setErrorMessageParams(errorMessageParams);
		errorPageData.setValidationErrors(validationErrors);
		if (errorMessageParams != null && errorMessageParams.length > 0) {
			errorPageData.setErrorMessage(errorMessageParams[0]);
		} else if (validationErrors != null && validationErrors.size() > 0) {
			errorPageData.setErrorMessage(validationErrors.get(0).toString());
		} else {
			errorPageData.setErrorMessage("Runtime Error Occurred. Please contact helpdesk!!");
		}
		this.status = new HttpStatusAdapter(HttpStatus.OK);
		this.header = header;
		this.data = errorPageData;
	}

	@Override
	public StatusType getStatus() {
		return status;
	}
}
