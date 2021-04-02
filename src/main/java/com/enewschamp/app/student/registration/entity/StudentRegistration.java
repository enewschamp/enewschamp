package com.enewschamp.app.student.registration.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.util.DigestUtils;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentRegistration")
public class StudentRegistration extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063853831579952336L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_generator")
	@SequenceGenerator(name = "student_id_generator", sequenceName = "student_id_seq", allocationSize = 1)
	@Column(name = "studentId", updatable = false, nullable = false)
	private Long studentId;

	@NotNull
	@Column(name = "studentKey", updatable = false, nullable = false)
	private String studentKey;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	@Column(name = "emailId", length = 200)
	private String emailId;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "password", length = 200)
	private String password;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "password1", length = 200)
	private String password1;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "password2", length = 200)
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

	@Column(name = "imageApprovalRequired", length = 1)
	private String imageApprovalRequired;

	@Column(name = "avatarName", length = 100)
	private String avatarName;

	@Column(name = "isTestUser", length = 1)
	private String isTestUser = "N";

	@Column(name = "forcePasswordChange", length = 1)
	private String forcePasswordChange = "N";

	@Column(name = "creationDateTime")
	private LocalDateTime creationDateTime;

	@Column(name = "fcmToken", length = 500)
	private String fcmToken;

	public String getKeyAsString() {
		return this.studentKey;
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		// if (operationDateTime == null) {
		operationDateTime = LocalDateTime.now();
		// }
		if (studentKey == null) {
			studentKey = generateStudentKey();
		}
	}

	public String generateStudentKey() {
		String hashString = LocalDate.now().toString() + emailId;
		return DigestUtils.md5DigestAsHex(hashString.getBytes());
	}
}
