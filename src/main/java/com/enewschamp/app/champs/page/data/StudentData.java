package com.enewschamp.app.champs.page.data;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentData implements Serializable {

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

}
