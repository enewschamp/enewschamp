package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
	@Column(name = "StudentId")
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
	@Column(name = "email", length = 99)
	private String email;

	@Column(name = "studentDetails", length = 1)
	private String studentDetails;

	@Column(name = "studentDetailsW", length = 1)
	private String studentDetailsW;

	@Column(name = "studentPhoto", length = 1)
	private String studentPhoto;

	@Column(name = "studentPhotoW", length = 1)
	private String studentPhotoW;

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

	@Column(name = "emailVerified", length = 1)
	private String emailVerified;

	@Column(name = "emailVerifiedW", length = 1)
	private String emailVerifiedW;

	@Column(name = "evalAvailed", length = 1)
	private String evalAvailed;

	@Column(name = "boUserComments", length = 999)
	private Long boUserComments;

	@Column(name = "boAuthComments", length = 999)
	private Long boAuthComments;
}
