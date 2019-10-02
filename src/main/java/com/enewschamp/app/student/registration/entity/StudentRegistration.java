package com.enewschamp.app.student.registration.entity;

import java.time.LocalDateTime;

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
@Table(name = "StudentRegistration")
@EqualsAndHashCode(callSuper = false)
public class StudentRegistration extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studReg_id_generator")
	@SequenceGenerator(name = "studReg_id_generator", sequenceName = "studRegId_seq", allocationSize = 1)
	@Column(name = "studRegId", updatable = false, nullable = false)
	private Long studRegId;

	@Id
	@NotNull
	@Column(name = "emailId", length = 80)
	private String emailId;

	@NotNull
	@Column(name = "password", length = 80)
	private String password;

	@Column(name = "loginFlag", length = 1)
	private String loginFlag;

	@Column(name = "status", length = 1)
	private RegistrationStatus status;

	@Column(name = "lastLoginTime")
	private LocalDateTime lastLoginTime;

}
