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

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "grade")
	private String grade;

	@Column(name = "school")
	private String school;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

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

	@Column(name = "featureProfileInChamps")
	private String featureProfileInChamps;

	@Column(name = "champCity")
	private String champCity;

	@Column(name = "champProfilePic")
	private String champProfilePic;

	@Column(name = "champSchool")
	private String champSchool;

	@Column(name = "imageApprovalRequired")
	private String imageApprovalRequired;

	@Column(name = "studentDetailsApprovalRequired")
	private String studentDetailsApprovalRequired;

	@Column(name = "schoolDetailsApprovalRequired")
	private String schoolDetailsApprovalRequired;

}
