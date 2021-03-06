package com.enewschamp.app.user.login.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "UserLogin", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "userId", "deviceId", "loginFlag" }) })
@EqualsAndHashCode(callSuper = false)
public class UserLogin extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userLogin_id_generator")
	@SequenceGenerator(name = "userLogin_id_generator", sequenceName = "userLogin_id_seq", allocationSize = 1)
	@Column(name = "userLoginId", updatable = false, nullable = false)
	private Long userLoginId;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	@Column(name = "userId", length = 80)
	private String userId;

	@Column(name = "loginFlag", length = 1)
	private String loginFlag;

	@Column(name = "lastLoginTime")
	private LocalDateTime lastLoginTime;

	@Column(name = "tokenExpirationTime")
	private LocalDateTime tokenExpirationTime;

	@Column(name = "tokenId", length = 50)
	private String tokenId;

	@Column(name = "deviceId", length = 50)
	private String deviceId;

	@Column(name = "appName", length = 50)
	private String appName;

	@Column(name = "appVersion", length = 50)
	private String appVersion;

	@Column(name = "userType")
	@Enumerated(EnumType.STRING)
	private UserType userType;

}