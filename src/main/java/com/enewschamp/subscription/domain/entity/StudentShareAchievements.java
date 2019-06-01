package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentShareAchievements")
public class StudentShareAchievements extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StudentShareAchievements_id_generator")
	@SequenceGenerator(name="StudentShareAchievements_id_generator", sequenceName = "StudentShareAchievements_seq", allocationSize=1)
	@Column(name = "StudentShareAchievementsId", length=10)
	private Long StudentShareAchievementsId= 0L;
	
	@NotNull
	@Column(name = "studentId", length=10)
	private Long studentId;
	
	@NotNull
	@Column(name = "personalMessage", length=200)
	private String personalMessage;
	
	
	@Column(name = "contact1", length=99)
	private String contact1;
	
	@Column(name = "contact2", length=99)
	private String contact2;
	
	@Column(name = "contact3", length=99)
	private String contact3;
	
	@Column(name = "contact4", length=99)
	private String contact4;
	
	@Column(name = "contact5", length=99)
	private String contact5;
	
	@Column(name = "contact6", length=99)
	private String contact6;
	
	@Column(name = "contact7", length=99)
	private String contact7;
	
	@Column(name = "contact8", length=99)
	private String contact8;
	
	@Column(name = "contact9", length=99)
	private String contact9;
	
	@Column(name = "contact10", length=99)
	private String contact10;
	
}
