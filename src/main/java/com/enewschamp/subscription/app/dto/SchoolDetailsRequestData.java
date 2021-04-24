package com.enewschamp.subscription.app.dto;

import lombok.Data;

@Data
public class SchoolDetailsRequestData {

	private String country;
	private String state;
	private String city;
	private String schoolProgramCode;
	private String approvalRequired;
}
