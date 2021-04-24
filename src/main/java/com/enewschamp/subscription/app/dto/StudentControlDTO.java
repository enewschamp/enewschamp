package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentControlDTO extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private Long studentId;
	private String studentDetails;
	private String schoolDetails;
	private String subscriptionType;
	private String preferences;
	private String emailIdVerified;
	private String evalAvailed;
	private Long boUserComments;
	private Long boAuthComments;

}