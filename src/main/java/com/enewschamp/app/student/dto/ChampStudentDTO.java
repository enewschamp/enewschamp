package com.enewschamp.app.student.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChampStudentDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image;
	private String name;
	private String surname;
	private String grade;
	private String school;
	private String city;
	private Long monthlyScore;
	//private Long studentID;
}
