package com.enewschamp.subscription.app.dto;

import java.util.Date;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentDetailsDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID= 0L;
	
	private String name;
	private String surname;
	
	private String otherName;
	private String gender;
	private Date dob;
	
	private Long mobileNumber;
	
	private String photo;
	
	private Long avtarID;
}
