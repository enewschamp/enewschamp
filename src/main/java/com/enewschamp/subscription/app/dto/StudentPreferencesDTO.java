package com.enewschamp.subscription.app.dto;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPreferencesDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentId;
	private String readingLevel;
	private String featureProfileInChamps;
	private ChampPermissionsDTO champPermissions;
	private StudentPreferencesCommDTO commsOverEmail;
}