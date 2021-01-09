
package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Genre", uniqueConstraints={@UniqueConstraint(columnNames={"nameId"})})
public class Genre extends BaseEntity {

	private static final long serialVersionUID = 4864561970205860047L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_id_generator")
	@SequenceGenerator(name = "genre_id_generator", sequenceName = "genre_id_seq", allocationSize = 1)
	@Column(name = "GenreId", length = 3)
	private Long genreId = 0L;

	@NotNull
	@Column(name = "nameId")
	private String nameId;

	@Column(name = "ImageName", length = 100)
	private String imageName;

	@Transient
	private String base64Image;

	@Transient
	private String imageTypeExt;

}
