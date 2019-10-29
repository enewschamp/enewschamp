package com.enewschamp.app.common;



import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class MaintenanceDTO extends AbstractDTO {
	
	@Size(max=ForeignKeyColumnLength.UserId)
	@NotNull
	protected String operatorId;
	
	private LocalDateTime operationDateTime;
	
	@NotNull
	private RecordInUseType recordInUse;
	
}
