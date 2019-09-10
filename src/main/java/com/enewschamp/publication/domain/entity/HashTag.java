package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="HashTag")
public class HashTag extends BaseEntity {
	
	private static final long serialVersionUID = 2844284287599966841L;

	@Id
	@NotNull
	@Column(name = "HashTag", length=25)
	private String hashTag;
	
	@NotNull
	@Column(name = "GenreId", length = ForeignKeyColumnLength.GenreId)
	private String genreId;
	
	@NotNull
	@Column(name = "LanguageId", length = 3)
	private String languageId;
	
	
}
