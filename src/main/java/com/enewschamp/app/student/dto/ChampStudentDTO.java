package com.enewschamp.app.student.dto;

import java.io.Serializable;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChampStudentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String name;

	@JsonInclude
	private String surname;

	@JsonInclude
	private String grade;

	@JsonInclude
	private String school;

	@JsonInclude
	private String city;

	@JsonInclude
	private Long monthlyScore;

	@JsonInclude
	private Long studentRank;

	@JsonInclude
	private String avatarName;

	@JsonInclude
	private String photoName;
}
