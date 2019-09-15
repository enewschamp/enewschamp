package com.enewschamp.app.common;



import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class TransactionDTO extends AbstractDTO {
	
	private static final long serialVersionUID = 4435891856977341938L;

	@Size(max=ForeignKeyColumnLength.UserId)
	protected String operatorId;
	
	private LocalDateTime operationDateTime;
	
	
}
