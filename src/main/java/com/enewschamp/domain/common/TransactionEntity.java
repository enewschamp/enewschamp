package com.enewschamp.domain.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class TransactionEntity implements IEntity, Serializable {

	private static final long serialVersionUID = -3997401196327905892L;

	@NotNull
	@Column(name = "OperatorId", length = ForeignKeyColumnLength.UserId)
	@DiffIgnore
	protected String operatorId;

	@NotNull
	@Column(name = "OperationDateTime")
	@DiffIgnore
	protected LocalDateTime operationDateTime;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (operationDateTime == null) {
			operationDateTime = LocalDateTime.now();
		}
	}

	public String getKeyAsString() {
		return "";
	};
}
