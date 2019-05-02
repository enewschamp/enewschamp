package com.enewschamp.app.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;

@Data
public abstract class AbstractDTO implements Serializable {
	
	@Size(max=ForeignKeyColumnLength.UserId)
	protected String operatorId;
	
	@NotNull
	private RecordInUseType recordInUse;
}
