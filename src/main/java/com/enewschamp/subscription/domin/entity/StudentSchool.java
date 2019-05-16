package com.enewschamp.subscription.domin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentSchool")
public class StudentSchool extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "StudentID", length=10)
	private long studentID = 0L;
	
	@NotNull
	@Column(name = "SchoolID", length=10)
	private long schoolId = 0L;
	
	@NotNull
	@Column(name="Grade, length=10")
	private String grade;
	
	@NotNull
	@Column(name="Section, length=20")
	private String section;
	
	
	
}
