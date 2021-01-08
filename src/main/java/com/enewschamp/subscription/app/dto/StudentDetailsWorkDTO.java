package com.enewschamp.subscription.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentDetailsWorkDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private Long studentId = 0L;

	@JsonInclude
	private String emailId;

	@JsonInclude
	private String name;

	@JsonInclude
	private String surname;

	@JsonInclude
	private String otherNames;

	@JsonInclude
	private String gender;

	@JsonInclude
	private LocalDate doB;

	@JsonInclude
	private String mobileNumber;
}
