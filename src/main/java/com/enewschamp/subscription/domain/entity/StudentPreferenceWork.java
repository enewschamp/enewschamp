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
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentPreference_Work")
public class StudentPreferenceWork {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name="StudentID", length=10)
	private Long studentID;
	
	@NotNull
	@Column(name="ReadingLevel", length=1)
	private String readingLevel;
	
	@Embedded
	private StudentPreferenceCommWork communications;
	
}
