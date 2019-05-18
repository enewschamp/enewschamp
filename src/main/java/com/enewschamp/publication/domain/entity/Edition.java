package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="Edition")
public class Edition extends BaseEntity {
	
	private static final long serialVersionUID = 4703046794189386714L;

	@Id
	@NotNull
	@Column(name = "EditionID", length=10)
	private String editionId;
	
	@NotNull
	@Column(name = "LanguageID", length = 3)
	private String languageId;
	
	@NotNull
	@Column(name = "EditionName", length = 255)
	private String editionName;
	
}
