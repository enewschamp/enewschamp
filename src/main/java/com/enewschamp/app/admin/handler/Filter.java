package com.enewschamp.app.admin.handler;

import lombok.Data;

@Data
public class Filter {
	private String countryId;
	private String stateId;
	private String name;
	private String newsEventsApplicable;
}
