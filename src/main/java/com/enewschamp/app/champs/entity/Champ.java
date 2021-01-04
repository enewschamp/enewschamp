package com.enewschamp.app.champs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Table(name = "champs_vw")
@Entity
@Immutable
public class Champ {

	@Id
	@Column(name = "studentId", updatable = false, nullable = false)
	private Long studentId;

	@Column(name = "studentName")
	private String studentName;

	@Column(name = "surname")
	private String surname;
	@Column(name = "grade")
	private String grade;

	@Column(name = "schoolName")
	private String schoolName;

	@Column(name = "cityName")
	private String cityName;

	@Column(name = "score")
	private Long score;

	@Column(name = "yearMonth")
	private Long yearMonth;

	@Column(name = "readingLevel")
	private Long readingLevel;

	@Column(name = "rank")
	private Long rank;

	@Column(name = "avatarName")
	private String avatarName;

	@Column(name = "photoName")
	private String photoName;
}
