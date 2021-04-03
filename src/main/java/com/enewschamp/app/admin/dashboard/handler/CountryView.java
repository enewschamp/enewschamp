package com.enewschamp.app.admin.dashboard.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface CountryView {
	@JsonProperty("id")
	String getNameId();
	@JsonProperty("name")
	String getDescription();

}
