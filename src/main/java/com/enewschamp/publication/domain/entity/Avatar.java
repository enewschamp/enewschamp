package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "Avatar", uniqueConstraints = { @UniqueConstraint(columnNames = { "nameId", "gender", "readingLevel" }) })
@EqualsAndHashCode(callSuper = false)
public class Avatar extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avatar_id_generator")
	@SequenceGenerator(name = "avatar_id_generator", sequenceName = "avatar_id_seq", allocationSize = 1)
	@Column(name = "avatarId", updatable = false, nullable = false)
	private Long avatarId = 0L;

	@NotNull
	@Column(name = "nameId", length = 50)
	private String nameId;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "gender", length = 20)
	private Gender gender;

	@NotNull
	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

	@Column(name = "imageName", length = 100)
	private String imageName;

	@Transient
	private String imageBase64;

	@Transient
	private String imageTypeExt;
}