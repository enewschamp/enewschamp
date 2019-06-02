package com.enewschamp.problem;

import java.net.URI;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.publication.domain.service.ValidationError;

@Immutable
public final class Fault extends ThrowableProblem {

	private static final long serialVersionUID = -6897678664880320263L;
	public static final String TYPE_VALUE = "https://com.enewschamp.api/problem/validation-failure";
	public static final URI TYPE = URI.create(TYPE_VALUE);

	private final URI type;
	private final StatusType status;
	private String errorCode;
	private String title;
	private HeaderDTO header;
	
	private List<ValidationError> validationErrors;

	public Fault(final StatusType status, final BusinessException e, final HeaderDTO header) {
		this(TYPE, status, e.getErrorCode(), e.getValidationErrors(), header);
	}
	
	public Fault(final StatusType status, final String errorCode, final HeaderDTO header) {
		this(TYPE, status, errorCode, null, header);
	}
	
	public Fault(final StatusType status, final BusinessException e) {
		this(TYPE, status, e.getErrorCode(), e.getValidationErrors(), null);
	}

	Fault(final URI type, final StatusType status, final String errorCode, 
		  List<ValidationError> validationErrors, final HeaderDTO header) {
		this.type = type;
		this.status = status;
		this.title = errorCode;
		this.errorCode = errorCode;
		this.header = header;
		this.validationErrors = validationErrors;
	}

	@Override
	public URI getType() {
		return type;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public StatusType getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	public HeaderDTO getHeader() {
		return header;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.title = errorMessage;
		if(header != null) {
			header.setFailureMessage(errorMessage);
		}
	}
	
	public List<ValidationError> getValidationErrors() {
		return this.validationErrors;
	}
	
}
