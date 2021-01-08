package com.enewschamp.subscription.app.dto;

import javax.persistence.Id;

import com.enewschamp.app.common.AbstractDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
//@Entity
public class SchoolProgramLOV extends AbstractDTO {

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
