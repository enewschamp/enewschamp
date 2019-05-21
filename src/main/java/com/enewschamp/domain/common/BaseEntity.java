package com.enewschamp.domain.common;



import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1459589314540791967L;

	@Column(name = "OperatorId", length=ForeignKeyColumnLength.UserId)
	protected String operatorId;

	@Column(name = "OperationDateTime")
	protected LocalDateTime operationDateTime;

	@Column(name = "RecordInUse", length = 1)
	@Enumerated(EnumType.STRING)
	private RecordInUseType recordInUse;
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(operationDateTime == null) {
			operationDateTime = LocalDateTime.now();
		}
	}
}
