package com.enewschamp.app.admin.dashboard.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface AvatarView {
	@JsonProperty("id")
	String getNameId();
	String getGender();
	String getReadingLevel();
	String getImageName();
}
