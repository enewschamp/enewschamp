package com.enewschamp.app.admin.dashboard.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GenreView {
	@JsonProperty("id")
	String getNameId();

	String getImageName();

}
