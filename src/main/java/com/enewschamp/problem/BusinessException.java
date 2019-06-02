package com.enewschamp.problem;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class BusinessException extends RuntimeException {


	private static final long serialVersionUID = 2017344958429707915L;
	
	private final String errorCode;


	public BusinessException(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}


}
