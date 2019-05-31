package com.enewschamp;

import org.javers.spring.auditable.AuthorProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class OperatorIdProvider implements AuthorProvider {

	@Override
	public String provide() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("mahendra", "welcome"));
		return "Mahendra";
	}

}
