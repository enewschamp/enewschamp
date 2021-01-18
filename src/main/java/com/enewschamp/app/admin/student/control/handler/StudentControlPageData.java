package com.enewschamp.app.admin.student.control.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentControlPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long studentId;
	private String emailId;
	private String studentDetails;
	private String schoolDetails;
	private String subscriptionType;
	private String preferences;
	private String emailIdVerified;
	private String evalAvailed;
	private Long boUserComments;
	private Long boAuthComments;
}