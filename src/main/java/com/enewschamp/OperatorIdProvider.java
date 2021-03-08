package com.enewschamp;

import org.javers.spring.auditable.AuthorProvider;

public class OperatorIdProvider implements AuthorProvider {

	private String operatorId;

	@Override
	public String provide() {
		// SecurityContextHolder.getContext()
		// .setAuthentication(new UsernamePasswordAuthenticationToken("mahendra",
		// "welcome"));
		return operatorId;
	}

}
