package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentPreferences")
public class StudentPreferences extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "ReadingLevel", length = 1)
	private String readingLevel;

	@NotNull
	@Column(name = "featureProfileInChamps", length = 1)
	private String featureProfileInChamps;

	@Embedded
	private ChampPermissions champPermissions;

	@Embedded
	private StudentPreferenceComm commsOverEmail;

}