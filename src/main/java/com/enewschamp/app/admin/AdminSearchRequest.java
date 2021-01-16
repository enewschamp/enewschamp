package com.enewschamp.app.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.enewschamp.app.user.login.entity.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminSearchRequest {
	private String countryId;
	private String cityId;
	private String stateId;
	private String name;
	private String surname;
	private String newsEventsApplicable;
	private Long studentId;
	private LocalDateTime createDateFrom;
	private LocalDateTime createDateTo;
	private String categoryId;
	private String closeFlag;
	private String supportUserId;
	private String schoolChainId;
	private String eduBoard;
	private String ownership;
	private String schoolProgram;
	private String schoolProgramCode;
	private String institutionId;
	private String institutionType;
	private String addressType;
	private String stakeholderId;
	private String schoolId;
	private String editionId;
	private String subscriptionType;
	private String autoRenewal;
	private LocalDate effectiveDateFrom;
	private LocalDate effectiveDateTo;
	private LocalDate startDateFrom;
	private LocalDate startDateTo;
	private LocalDate endDateFrom;
	private LocalDate endDateTo;
	private String studentBadgesId;
	private String yearMonth;
	private String badgeId;
	private String userId;
	private String approvalStatus;
	private String roleId;
	private String role;
	private String pageName;
    private Long userLoginId;
    private UserType userType;
    private String deviceId;
    private String loginFlag;
    private String emailId;
    private String phoneNumber;
    private String verified;
    private Long navId;
    private Long ruleId;
    private String currentPage;
    private String operation;
    private String action;
    private String isPremiumFeature;
    private String globalControlRef;
    private String controlName;
    private Long uiControlGlobalId;
    private Long uiControlId;
    private String executionSequence;
    private String rule;
    private String visibility;
    private Long propertyId;
    private String appName;
    private Long errorCodeId;
    private String errorCategory;
    private LocalDate publicationDate;
    private String readingLevel;
    private Long genreId;
    private String scoreYearMonth;
}
