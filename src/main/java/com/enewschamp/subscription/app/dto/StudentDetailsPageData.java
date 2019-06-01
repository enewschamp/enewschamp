package com.enewschamp.subscription.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentDetailsPageData  extends PageData {

	
	private static final long serialVersionUID = 1L;

	private Long studentID= 0L;
	private String emailID;

	private String name;
	private String surname;
	
	private String otherName;
	private String gender;
	private LocalDate dob;
	
	private String mobileNumber;
	
	private String photo;
	
	private Long avtarID;
	private String incompeleteFormText;
	private String correctDetailsText;
	private String dobText;
	private String dobReasonText;
	

}
