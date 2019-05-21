package com.enewschamp.problem;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.zalando.problem.StatusType;

import lombok.Data;

@Data
public class HttpStatusAdapter implements StatusType {

	private final HttpStatus status;

	public HttpStatusAdapter(final HttpStatus status) {
		this.status = status;
	}

	@Override
	public int getStatusCode() {
		return status.value();
	}

	@Override
	public String getReasonPhrase() {
		return status.getReasonPhrase();
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		} else if (that instanceof HttpStatusAdapter) {
			final HttpStatusAdapter other = (HttpStatusAdapter) that;
			return Objects.equals(status, other.status);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(status);
	}
}
