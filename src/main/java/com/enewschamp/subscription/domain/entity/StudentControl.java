package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentControl")
public class StudentControl extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@Column(name = "StudentDetails", length = 1)
	private String studentDetails;

	@Column(name = "SchoolDetails", length = 1)
	private String schoolDetails;

	@Column(name = "SubscriptionType", length = 1)
	private String subscriptionType;

	@Column(name = "Preferences", length = 1)
	private String preferences;

	@Column(name = "emailIdVerified", length = 1)
	private String emailIdVerified;

	@Column(name = "EvalAvailed", length = 1)
	private String evalAvailed;

	@Column(name = "BOUserComments", length = 999)
	private Long boUserComments;

	@Column(name = "BOAuthComments", length = 999)
	private Long boAuthComments;

}