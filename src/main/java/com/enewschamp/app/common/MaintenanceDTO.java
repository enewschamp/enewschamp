package com.enewschamp.app.common;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class MaintenanceDTO extends AbstractDTO {

	//@JsonProperty(access = Access.WRITE_ONLY)
	@Size(max = ForeignKeyColumnLength.UserId)
	protected String operatorId;

	//@JsonProperty(access = Access.WRITE_ONLY)
	private LocalDateTime operationDateTime;

	//@JsonProperty(access = Access.WRITE_ONLY)
	private RecordInUseType recordInUse;

}
