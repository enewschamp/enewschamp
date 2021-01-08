package com.enewschamp.app.student.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class ChampStudentMYDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonInclude
	private Long studentId;

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
	private Long score;

	@JsonInclude
	private Long rank;

	@JsonInclude
	private String avatarName;

	@JsonInclude
	private String photoName;
}
