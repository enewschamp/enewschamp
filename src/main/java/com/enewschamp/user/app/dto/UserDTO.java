package com.enewschamp.user.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.domain.common.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	@Size(max = 8)
	private String userId;

	@Column(name = "title")
	private String title;

	@NotNull
	@Size(max = 50)
	private String name;

	@NotNull
	@Size(max = 50)
	private String surname;

	@Size(max = 100)
	private String otherNames;

	@NotNull
	private Gender gender;

	private LocalDate doB;

	@NotNull
	private LocalDate contractStartDate;

	@NotNull
	private LocalDate contractEndDate;

	@NotNull
	private String mobileNumber1;

	private String mobileNumber2;

	private String landline1;

	private String landline2;

	@NotNull
	@Size(max = 99)
	private String emailId1;

	private String emailId2;

	private String comments;

	private String password;

	@JsonInclude
	private String imageName;

	private String imageBase64;

	private String imageTypeExt = "jpg";

	private String imageUpdate;

	@JsonInclude
	private String theme;

	@JsonInclude
	private String fontHeight;

	private LocalDateTime creationDateTime;
}