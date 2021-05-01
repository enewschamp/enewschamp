package com.enewschamp.app.admin.user.login.handler;

import java.sql.Blob;
import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserActivityTrackerPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long userLoginId;
	private String userId;
	private String deviceId;
	private UserType userType;
	private String actionPerformed;
	private LocalDateTime actionTime;
	private UserAction actionStatus;
	private String errorCode;
	private String errorDescription;
	private Blob requestData;
	private Blob errorText;
}
