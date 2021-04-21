package com.enewschamp.subscription.app.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class SchoolProgramLOV implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String name;
	private String schoolProgramCode;
	private String city;
	private String cityName;
	private String state;
	private String stateName;
	private String country;
	private String countryName;
}