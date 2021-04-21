package com.enewschamp.app.user.login.entity;

import java.sql.Blob;
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
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "UserActivityTracker")
@EqualsAndHashCode(callSuper = false)
public class UserActivityTracker extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userActivityTracker_id_generator")
	@SequenceGenerator(name = "userActivityTracker_id_generator", sequenceName = "userActivityTracker_id_seq", allocationSize = 1)
	@Column(name = "userLoginAuditId", updatable = false, nullable = false)
	private Long userLoginId;

	@Convert(converter = StringCryptoConverter.class)
	@NotNull
	@Column(name = "userId", length = 80)
	private String userId;

	@Column(name = "deviceId", length = 50)
	private String deviceId;

	@Column(name = "userType")
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column(name = "actionPerformed", length = 50)
	private String actionPerformed;

	@Column(name = "actionTime")
	private LocalDateTime actionTime;

	@Column(name = "actionStatus")
	@Enumerated(EnumType.STRING)
	private UserAction actionStatus;

	@Column(name = "errorCode", length = 50)
	private String errorCode;

	@Column(name = "errorDescription", length = 500)
	private String errorDescription;

	@Column(name = "requestData")
	private Blob requestData;

	@Column(name = "errorText")
	private Blob errorText;

}