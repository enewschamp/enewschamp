package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.time.LocalDateTime;

import com.enewschamp.subscription.app.dto.StudentPaymentDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime" })
public class StudentPaymentNilDTO extends StudentPaymentDTO {
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;
}
