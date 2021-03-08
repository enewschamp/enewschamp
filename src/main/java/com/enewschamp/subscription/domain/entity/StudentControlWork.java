package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
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
@Table(name = "StudentControlWork")
public class StudentControlWork extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "nextPageName", length = 99)
	private String nextPageName;

	@NotNull
	@Column(name = "nextPageOperation", length = 99)
	private String nextPageOperation;

	@Column(name = "nextPageLoadMethod", length = 99)
	private String nextPageLoadMethod;

	@NotNull
	@Column(name = "emailId", length = 99)
	private String emailId;

	@Column(name = "studentDetails", length = 1)
	private String studentDetails;

	@Column(name = "studentDetailsW", length = 1)
	private String studentDetailsW;

	@Column(name = "schoolDetails", length = 1)
	private String schoolDetails;

	@Column(name = "schoolDetailsW", length = 1)
	private String schoolDetailsW;

	@Column(name = "subscriptionType", length = 1)
	private String subscriptionType;

	@Column(name = "subscriptionTypeW", length = 1)
	private String subscriptionTypeW;

	@Column(name = "preferences", length = 1)
	private String preferences;

	@Column(name = "preferencesW", length = 1)
	private String preferencesW;

	@Column(name = "emailIdVerified", length = 1)
	private String emailIdVerified;

	@Column(name = "emailIdVerifiedW", length = 1)
	private String emailIdVerifiedW;

	@Column(name = "evalAvailed", length = 1)
	private String evalAvailed;

	@Column(name = "boUserComments", length = 999)
	private Long boUserComments;

	@Column(name = "boAuthComments", length = 999)
	private Long boAuthComments;
}
