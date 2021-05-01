package com.enewschamp.app.admin.dashboard.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EditionView {
	String getEditionId();

	String getEditionName();

	@JsonProperty("language")
	String getLanguageId();
}
