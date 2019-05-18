package com.enewschamp.subscription.domin.entity;

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
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentControl")
public class StudentControl extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StudentControl_id_generator")
	@SequenceGenerator(name="StudentControl_id_generator", sequenceName = "StudentControl_seq", allocationSize=1)
	@Column(name = "StudentID", length=10)
	private Long studentID= 0L;
	
	@NotNull
	@Column(name = "eMail", length=99)
	private String eMail;
	
	@NotNull
	@Column(name = "StudentDetails", length=1)
	private String studentDetails;
	
	@Column(name = "StudentPhoto", length=1)
	private String studentPhoto;
	@NotNull
	@Column(name = "SchoolDetails", length=1)
	private String schoolDetails;
	
	@Column(name = "SubscriptionType", length=1)
	private String subscriptionType;
	
	@Column(name = "Preferences", length=1)
	private String preferences;
	
	@Column(name = "eMailVerified", length=1)
	private String eMailVerified;
	
	@Column(name = "EvalAvailed", length=1)
	private String evalAvailed;
	
	@NotNull
	@Column(name = "BOUserComments", length=999)
	private Long boUserComments;
	
	@NotNull
	@Column(name = "BOAuthComments", length=999)
	private Long boAuthComments;
	
}