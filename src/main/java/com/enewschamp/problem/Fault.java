package com.enewschamp.problem;

import java.net.URI;

import javax.annotation.concurrent.Immutable;

import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

@Immutable
public final class Fault extends ThrowableProblem {

	private static final long serialVersionUID = -6897678664880320263L;
	public static final String TYPE_VALUE = "https://com.enewschamp.api/problem/validation-failure";
	public static final URI TYPE = URI.create(TYPE_VALUE);

	private final URI type;
	private final StatusType status;
	private final String occursWhen;
	private final String title;

	public Fault(final StatusType status, final String title, final String occursWhen) {
		this(TYPE, status, title, occursWhen);
	}

	Fault(final URI type, final StatusType status, final String title, final String occursWhen) {
		this.type = type;
		this.status = status;
		this.title = title;
		this.occursWhen = occursWhen;
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

	public String getOCccursWhen() {
		return occursWhen;
	}

}
