package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.common.state.dto.StateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operationDateTime", "keyAsString" })
public class StateNilDTO extends StateDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalDateTime lastUpdate;

}
