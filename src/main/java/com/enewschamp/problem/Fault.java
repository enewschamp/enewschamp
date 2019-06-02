package com.enewschamp.problem;

import java.net.URI;

import javax.annotation.concurrent.Immutable;

import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import com.enewschamp.app.common.HeaderDTO;

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

	public Fault(final StatusType status, final String errorCode, final HeaderDTO header) {
		this(TYPE, status, errorCode, header);
	}
	
	public Fault(final StatusType status, final String errorCode) {
		this(TYPE, status, errorCode, null);
	}

	Fault(final URI type, final StatusType status, final String errorCode, final HeaderDTO header) {
		this.type = type;
		this.status = status;
		this.title = errorCode;
		this.errorCode = errorCode;
		this.header = header;
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
	
}
