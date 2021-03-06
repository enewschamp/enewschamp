package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Edition", uniqueConstraints = { @UniqueConstraint(columnNames = { "editionId", "languageId" }) })
public class Edition extends BaseEntity {

	private static final long serialVersionUID = 4703046794189386714L;

	@Id
	@NotNull
	@Column(name = "EditionId", length = ForeignKeyColumnLength.EditionId)
	private String editionId;

	@NotNull
	@Column(name = "EditionName", length = 255)
	private String editionName;

	@NotNull
	@Column(name = "LanguageId", length = 3)
	private String languageId;

}