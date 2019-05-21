package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentSchoolWorkDTO extends AbstractDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long studentID = 0L;
	
	private long schoolId = 0L;
	
	private String grade;
	
	private String section;
}
