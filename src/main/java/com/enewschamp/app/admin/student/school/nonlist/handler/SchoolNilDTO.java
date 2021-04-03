package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.school.dto.SchoolDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime" })
public class SchoolNilDTO extends SchoolDTO {
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;

}
