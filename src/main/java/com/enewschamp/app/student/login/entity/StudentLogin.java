package com.enewschamp.app.student.login.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.student.registration.entity.RegistrationStatus;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "StudentLogin")
@EqualsAndHashCode(callSuper = false)
public class StudentLogin extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentLogin_id_generator")
	@SequenceGenerator(name = "studentLogin_id_generator", sequenceName = "studentLogin_id_seq", allocationSize = 1)
	@Column(name = "studentLoginId", updatable = false, nullable = false)
	private Long studentLoginId;

	
	@NotNull
	@Column(name = "emailId", length = 80)
	private String emailId;

	
	@Column(name = "loginFlag", length = 1)
	private String loginFlag;

	@Column(name = "status", length = 1)
	private String status;

	@Column(name = "lastLoginTime")
	private LocalDateTime lastLoginTime;
	
	@Column(name = "deviceId", length=50)
	private String deviceId;
	

}
