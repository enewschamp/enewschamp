package com.enewschamp.problem;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class FaultManager extends RuntimeException {


	private static final long serialVersionUID = 2017344958429707915L;
	
	private final String errorCode;


	public FaultManager(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}


}
