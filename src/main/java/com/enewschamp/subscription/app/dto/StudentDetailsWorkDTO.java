package com.enewschamp.subscription.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentDetailsWorkDTO extends AbstractDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentID= 0L;
	private String emailId;

	private String name;
	private String surname;
	
	private String otherName;
	private String gender;
	private LocalDate dob;
	
	private String mobileNumber;
	
	private String photo;
	
	private Long avtarID;
}
