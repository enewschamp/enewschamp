package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.student.registration.dto.StudentRegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime" })
public class StudentRegistrationNilDTO extends StudentRegistrationDTO {
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;
}
