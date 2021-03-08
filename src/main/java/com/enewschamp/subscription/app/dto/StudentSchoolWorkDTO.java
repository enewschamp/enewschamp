package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSchoolWorkDTO extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentId = 0L;
	private String country;
	private String countryNotInTheList;
	private String state;
	private String stateNotInTheList;
	private String city;
	private String cityNotInTheList;
	private String school;
	private String schoolNotInTheList;
	private String section;
	private String grade;
	private String approvalRequired;
}
