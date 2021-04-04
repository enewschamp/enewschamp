package com.enewschamp.app.admin.student.details.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentDetailsPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long studentId;

	@NotNull(message = MessageConstants.STUDENT_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.STUDENT_NAME_NOT_EMPTY)
	private String name;

	@NotNull(message = MessageConstants.SURNAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.SURNAME_NOT_EMPTY)
	private String surname;

	private String otherNames;
	private String gender;

	@NotNull(message = MessageConstants.DOB_NOT_NULL)
	private LocalDate doB;

	private String mobileNumber;
	private String approvalRequired;

}
