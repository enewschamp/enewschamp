package com.enewschamp.app.student.badges.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="StudentBadges",uniqueConstraints={@UniqueConstraint(columnNames = {"studentId" , "editionId","badgeyearMonth","badgeId"})})
@EqualsAndHashCode(callSuper=false)
public class StudentBadges extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentBadges_id_generator")
	@SequenceGenerator(name="studentBadges_id_generator", sequenceName = "studentBadges_seq", allocationSize=1)
	@Column(name = "studentBadgesId", updatable = false, nullable = false)
	private Long studentBadgesId;
	
	@NotNull
	@Column(name = "studentId", length=10)
	private Long studentId;
	
	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;
	
	@Column(name = "badgeyearMonth", length=4)
	private Long yearMonth;
	
	@Column(name = "badgeId", length=3)
	private String badgeId;
	
	@Lob
	@Column(name = "badgeImage")
	private String badgeImage;
	
	
}
