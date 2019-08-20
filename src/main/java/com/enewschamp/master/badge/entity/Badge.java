package com.enewschamp.master.badge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="Badge")
@EqualsAndHashCode(callSuper=false)
public class Badge extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "badgeId", length=3)
	private String badgeId;
	
	@NotNull
	@Column(name = "nameId", length=10)
	private String nameId;
	
	@NotNull
	@Column(name = "name", length=50)
	private String name;

	@NotNull
	@Column(name = "genreId", length=12)
	private String genreId;
	
	@NotNull
	@Column(name = "monthlyPointsToScore", length=3)
	private Long monthlyPointsToScore;
	
	@Lob
	@Column(name = "image")
	private String image;
	
	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;
	
	
}
