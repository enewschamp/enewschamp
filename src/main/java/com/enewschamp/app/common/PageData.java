package com.enewschamp.app.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageData implements Serializable {

	@JsonInclude
	protected String operatorId;
	@JsonInclude
	protected LocalDateTime lastUpdate;
	@JsonInclude
	protected RecordInUseType recordInUse;

}
