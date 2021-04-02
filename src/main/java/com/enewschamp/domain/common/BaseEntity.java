package com.enewschamp.domain.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity implements IEntity, Serializable {
	private static final long serialVersionUID = 1459589314540791967L;

	@NotNull
	@Column(name = "operatorId", length = ForeignKeyColumnLength.UserId)
	@DiffIgnore
	protected String operatorId;

	@NotNull
	@Column(name = "operationDateTime")
	@DiffIgnore
	protected LocalDateTime operationDateTime;

	@NotNull
	@Column(name = "recordInUse", length = 1)
	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private RecordInUseType recordInUse;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		operationDateTime = LocalDateTime.now();
	}

	public String getKeyAsString() {
		return "";
	};
}
