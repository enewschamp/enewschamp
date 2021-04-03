package com.enewschamp.app.common;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class MaintenanceDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Size(max = ForeignKeyColumnLength.UserId)
	protected String operatorId;

	@JsonProperty(access = Access.WRITE_ONLY)
	private LocalDateTime operationDateTime;

	@JsonProperty(access = Access.WRITE_ONLY)
	private RecordInUseType recordInUse;
}
