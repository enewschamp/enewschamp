package com.enewschamp.app.otp.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "OTP")
@EqualsAndHashCode(callSuper = false)
public class OTP extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_id_generator")
	@SequenceGenerator(name = "otp_id_generator", sequenceName = "otp_seq", allocationSize = 1)
	@Column(name = "otpId", updatable = false, nullable = false)
	private Long otpId;

	@Column(name = "emailId", length = 80)
	private String emailId;

	@Column(name = "phoneNo", length = 15)
	private String phoneNo;

	@NotNull
	@Column(name = "otp")
	private String otp;

	@NotNull
	@Column(name = "otpGenTime")
	private LocalDateTime otpGenTime;

	@Column(name = "verified", length = 1)
	private String verified;

	@Column(name = "verificationTime")
	private LocalDateTime verificationTime;

	@NotNull
	@Column(name = "verifyAttempts", length = 1)
	private int verifyAttempts = 0;

	@PrePersist
	public void prePersist() {
		if (verified == null) {
			verified = "N";
		}
	}
}
