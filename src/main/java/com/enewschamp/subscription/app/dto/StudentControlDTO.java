package com.enewschamp.subscription.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentControlDTO extends AbstractDTO {
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
