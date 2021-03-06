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
@Table(name = "Badge", uniqueConstraints = { @UniqueConstraint(columnNames = { "editionId", "genreId", "nameId" }) })
public class Badge extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "badge_id_generator")
	@SequenceGenerator(name = "badge_id_generator", sequenceName = "badge_id_seq", allocationSize = 1)
	@Column(name = "badgeId", length = 3)
	private Long badgeId = 0L;

	@NotNull
	@Column(name = "nameId", length = 10)
	private String nameId;

	@Column(name = "genreId", length = 20)
	private String genreId;

	@NotNull
	@Column(name = "monthlyPointsToScore", length = 3)
	private Long monthlyPointsToScore;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

	@Column(name = "imageName", length = 100)
	private String imageName;

	@Column(name = "successImageName", length = 100)
	private String successImageName;

	@Column(name = "audioFileName", length = 100)
	private String audioFileName;

	@Transient
	private String imageBase64;

	@Transient
	private String successImageBase64;

	@Transient
	private String audioFileBase64;

	@Transient
	private String imageTypeExt;

	@Transient
	private String successImageTypeExt;

	@Transient
	private String audioFileTypeExt;

}