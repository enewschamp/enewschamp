package com.enewschamp.domain.common;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1459589314540791967L;

	@NotNull
	@Column(name = "OperatorId", length=ForeignKeyColumnLength.UserId)
	protected String operatorId;

	@NotNull
	@Column(name = "OperationDateTime")
	protected Timestamp operationDateTime = new Timestamp(System.currentTimeMillis());

	@NotNull
	@Column(name = "RecordInUse", length = 1)
	@Enumerated(EnumType.STRING)
	private RecordInUseType recordInUse;
}
