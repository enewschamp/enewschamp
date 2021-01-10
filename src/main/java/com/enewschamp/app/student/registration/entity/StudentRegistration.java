package com.enewschamp.app.student.registration.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentRegistration", uniqueConstraints = { @UniqueConstraint(columnNames = { "emailId" }) })
public class StudentRegistration extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_generator")
	@SequenceGenerator(name = "student_id_generator", sequenceName = "student_id_seq", allocationSize = 1)
	@Column(name = "studentId")
	private long studentId = 0L;

	@NotNull
	@Column(name = "emailId", length = 80)
	private String emailId;

	@Column(name = "password", length = 80)
	private String password;

	@Column(name = "password1", length = 80)
	private String password1;

	@Column(name = "password2", length = 80)
	private String password2;

	@Column(name = "isDeleted", length = 1)
	private String isDeleted;

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

	@Column(name = "photoName", length = 100)
	private String photoName;

	@Column(name = "avatarName", length = 100)
	private String avatarName;

	@Column(name = "isTestUser", length = 1)
	private String isTestUser = "N";

	@Column(name = "forcePasswordChange", length = 1)
	private String forcePasswordChange = "N";

	@Column(name = "creationDateTime")
	private LocalDateTime creationDateTime;
}
