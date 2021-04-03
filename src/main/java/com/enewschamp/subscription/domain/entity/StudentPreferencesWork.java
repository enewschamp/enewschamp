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
@Table(name = "StudentPreferencesWork")
public class StudentPreferencesWork extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "StudentId", length = 10)
	private Long studentId;

	@NotNull
	@Column(name = "ReadingLevel", length = 1)
	private String readingLevel;

	@Embedded
	private StudentPreferenceCommWork commsOverEmail;

}
