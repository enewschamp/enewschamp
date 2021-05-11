package com.enewschamp.app.admin.publication.champs.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse" })
public class StudentChampsPageData extends PageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2320151025863877182L;
	private Long studentId;
	private String name;
	private String surname;
	private String grade;
	private String school;
	private String city;
	private String state;
	private String country;
	private Long score;
	private Long rank;
	private String avatarName;
	private String photoName;
	private String featureProfileInChamps;
	private String champCity;
	private String champProfilePic;
	private String champSchool;
	private String imageApprovalRequired;
	private String studentDetailsApprovalRequired;
	private String schoolDetailsApprovalRequired;

}
