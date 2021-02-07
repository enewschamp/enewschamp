package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.time.LocalDateTime;

import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime" })
public class StudentControlNilDTO extends StudentControlDTO {
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;
}