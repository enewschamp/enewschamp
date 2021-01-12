package com.enewschamp.user.app.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLeaveKeyDTO implements Serializable {

	private static final long serialVersionUID = 8946600292510794786L;

	@NotNull(message = MessageConstants.USER_ID_NOT_NULL)
	@Size(max = ForeignKeyColumnLength.UserId)
	@NotEmpty(message = MessageConstants.USER_ID_NOT_EMPTY)
	private String userId;

	@NotNull(message = MessageConstants.START_DATE_NOT_NULL)
	private LocalDate startDate;

}
