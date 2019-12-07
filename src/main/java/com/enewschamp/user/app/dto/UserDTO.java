package com.enewschamp.user.app.dto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.domain.common.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	@Size(max = 8)
	private String userId;

	@Column(name = "Title")
	private Long title;

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

	@NotNull
	private LocalDate contractStartDate;

	@NotNull
	private LocalDate contractEndDate;

	@NotNull
	private long mobile1 = 0;

	private long mobile2 = 0;

	private long landLine1 = 0;

	private long landLine2 = 0;

	@NotNull
	@Size(max = 99)
	private String email1;

	private String email2;

	private String comments;

	private String password;

	private String photo;

}
