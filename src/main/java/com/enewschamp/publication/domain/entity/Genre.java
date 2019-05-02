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
@Table(name="Genre")
public class Genre extends BaseEntity {

	private static final long serialVersionUID = 4864561970205860047L;

	@Id
	@NotNull
	@Column(name = "GenreID", length=12)
	private String genreId;
	
	@NotNull
	@Column(name = "NameID")
	private Long nameId;
	
	@NotNull
	@Column(name = "ImagePath", length = 200)
	private String imagePath;
	
}
