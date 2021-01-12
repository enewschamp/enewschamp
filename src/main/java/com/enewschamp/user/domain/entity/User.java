package com.enewschamp.user.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "User",uniqueConstraints={@UniqueConstraint(columnNames={"name", "surname"})})
public class User extends BaseEntity {

	private static final long serialVersionUID = -7423524921019152899L;

	@Id
	@NotNull
	@Column(name = "userId", length = 8)
	private String userId;

	@NotNull
	@Column(name = "title")
	private String title;

	@NotNull
	@Column(name = "name", length = 50)
	private String name;

	@NotNull
	@Column(name = "surname", length = 50)
	private String surname;

	@Column(name = "otherNames", length = 100)
	private String otherNames;

	@Enumerated(EnumType.STRING)
	@DiffIgnore
	@Column(name = "gender", length = 1)
	private Gender gender;

	@Column(name = "DoB")
	private LocalDate doB;

	@NotNull
	@Column(name = "contractStartDate")
	private LocalDate contractStartDate;

	@NotNull
	@Column(name = "contractEndDate")
	private LocalDate contractEndDate;

	@NotNull
	@Column(name = "mobileNumber1", length = 15)
	private String mobileNumber1;

	@Column(name = "mobileNumber2", length = 15)
	private String mobileNumber2;

	@Column(name = "landline1", length = 12)
	private String landline1;

	@Column(name = "landline2", length = 12)
	private String landline2;

	@NotNull
	@Column(name = "emailId1", length = 99)
	private String emailId1;

	@Column(name = "emailId2", length = 99)
	private String emailId2;

	@Column(name = "comments", length = 999)
	private String comments;

	@Transient
	private String base64Image;

	@Transient
	private String imageTypeExt;

	@Column(name = "password", length = 80)
	private String password;

	@Column(name = "password1", length = 80)
	private String password1;

	@Column(name = "password2", length = 80)
	private String password2;

	@NotNull
	@Column(name = "isAccountLocked", length = 1)
	private String isAccountLocked = "N";

	@DiffIgnore
	@Column(name = "lastSuccessfulLoginAttempt")
	private LocalDateTime lastSuccessfulLoginAttempt;

	@DiffIgnore
	@Column(name = "lastUnsuccessfulLoginAttempt")
	private LocalDateTime lastUnsuccessfulLoginAttempt;

	@NotNull
	@Column(name = "incorrectLoginAttempts", length = 1)
	private long incorrectLoginAttempts = 0;

	@NotNull
	@Column(name = "isActive", length = 1)
	private String isActive = "Y";
	
	@Column(name = "theme", length = 1)
	private String theme;

	@Column(name = "fontHeight", length = 1)
	private String fontHeight;

	@Column(name = "ImageName", length = 100)
	private String imageName;

	@Column(name = "creationDateTime")
	private LocalDateTime creationDateTime;
}
