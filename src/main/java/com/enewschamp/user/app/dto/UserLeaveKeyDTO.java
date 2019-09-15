package com.enewschamp.user.app.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserLeaveKeyDTO implements Serializable {

	private static final long serialVersionUID = 8946600292510794786L;

	@NotNull
	@Size(max = ForeignKeyColumnLength.UserId)
	private String userId;

	@NotNull
	private LocalDate startDate;	
	

}
