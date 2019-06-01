package com.enewschamp.app.common;



import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.dto.FieldChangeDTO;
import com.enewschamp.domain.common.OperationType;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class MaintenanceDTO extends AbstractDTO {
	
	@Size(max=ForeignKeyColumnLength.UserId)
	protected String operatorId;
	
	private LocalDateTime operationDateTime;
	
	@NotNull
	private RecordInUseType recordInUse;
	
}
