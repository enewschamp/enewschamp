package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.admin.student.school.handler.StudentSchoolPageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime"})
public class StudentSchoolNilDTO extends StudentSchoolPageData{
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;
}
