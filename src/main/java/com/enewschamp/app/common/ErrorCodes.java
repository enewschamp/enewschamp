package com.enewschamp.app.common;

public interface ErrorCodes {

	// Error codes for publication APIs start
	public static String ARTICLE_GROUP_NOT_FOUND = "PUB_001";
	public static String ARTICLE_NOT_FOUND = "PUB_002";
	public static String EDITION_NOT_FOUND = "PUB_003";
	public static String GENRE_NOT_FOUND = "PUB_004";
	public static String HASH_TAG_NOT_FOUND = "PUB_005";
	public static String QUIZ_NOT_FOUND = "PUB_006";
	public static String PUBLICATION_NOT_FOUND = "PUB_007";
	public static String PUBLICATION_ART_LINK_NOT_FOUND = "PUB_008";
	public static String PUBLICATION_GRP_NOT_FOUND = "PUB_009";
	public static String INVALID_ARTICLE_ID = "PUB_010";
	public static String ARTICLE_ID_CHANGED = "PUB_011";
	public static String PUBLICATION_DATE_SHOULD_BE_FUTURE = "PUB_012";
	public static String INVALID_ARTICLE_ACTION = "PUB_013";
	public static String QUIZ_LIMIT_EXCEEDED = "PUB_014";
	public static String PUBLICATION_DAILY_SUMMARY_NOT_FOUND = "PUB_015";
	public static String PUBLICATION_MONTHLY_SUMMARY_NOT_FOUND = "PUB_016";
	public static String RATING_REQD_FOR_PUBLISH = "PUB_017";
	public static String REWORK_COMMENTS_REQUIRED = "PUB_018";
	public static String INVALID_USERNAME_OR_PASSWORD="PUB_019";
	// Error codes for publication APIs end

	public static String STUDENT_DTLS_NOT_FOUND = "STUDDTLS_001";
	
	public static String TREND_DAILY_NOT_FOUND ="TRDLY_001";
	public static String TREND_MONTHLY_TOTAL_NOT_FOUND ="TRMNTT_001";
	public static String BADGE_NOT_FOUND ="BDG_001";
	public static String QUIZ_SCORE_NOT_FOUND ="QZSC_001";
	public static String STUDENT_ACTIVITY_NOT_FOUND ="STA_001";

	public static String USER_NOT_FOUND = "USER_001";
	public static String USER_ROLE_NOT_FOUND = "USER_002";
	public static String USER_LEAVE_NOT_FOUND = "USER_003";
	public static String ROLE_NOT_ASSIGNED_TO_USER = "USER_004";
	public static String USER_IS_INACTIVE = "USER_005";
	public static String INVALID_USER_ID = "USER_006";

	public static String PAGE_NOT_FOUND= "PAGE_001";
	public static String NEXT_PAGE_NOT_FOUND= "PAGE_002";
	
	public static String COUNTRY_NOT_FOUND ="COU_001";
	public static String SCHOOL_PRICING_NOT_FOUND ="SPOU_001";
	public static String STUDENT_SHARE_ACHIEVEMENTS_NOT_FOUND ="SSANF_001";

	public static String INDIVIDUAL_PRICING_NOT_FOUND ="IPOU_001";

	public static String STATE_NOT_FOUND ="STATE_001";
	public static String CITY_NOT_FOUND ="CITY_001";
	public static String SCHOOL_NOT_FOUND ="SPOU_002";
	public static String REQUESTID_NOT_FOUND ="REQ_001";

	public static String SREVER_ERROR="SERVER_ERROR";
	
	//Framework error codes start
	public static String BUS_POLICY_FAILED="FRW_001";
	public static String STATUS_TRANSITION_NOT_FOUND="FRW_002";
	public static String MULTI_LANG_TEXT_NOT_FOUND = "FRW_003";
	public static String LOV_NOT_FOUND="FRW_004";
	public static String NOTIFICATION_ACTION_NOT_FOUND = "FRW_005";
	public static String ACTION_DISALLOWED_FOR_THIS_USER="FRW_006";
	//Framework error codes end
	
	public static String STUD_BADGES_NOT_FOUND="BADGE_001";
	
	public static String INVALID_YEARMONTH_FORMAT="TREND_001";
	public static String INVALID_YEAR="TREND_002";
	public static String INVALID_MONTH="TREND_003";

	public static String INVALID_NOT_PRESENT="SNA_01";
	public static String HOLIDAY_NOT_FOUND="HOL_01";
	public static String WORKING_HOUR_NOT_FOUND="WH_01";
	
	public static String MOBILE_IS_MANDATORY="MOB_01";

	public static String NOTIFICATION_NOT_FOUND="NOT_01";
	public static String STUDENT_NOTIFICATION_NOT_FOUND = "ST_NOT_01";
	public static String EMAIL_NOT_SENT="EM_NOT_01";
	public static String NO_RECORD_FOUND="NO_REC_01";
	public static String OTP_EXPIRED="OTP_01";
	public static String STUD_REG_NOT_FOUND="STUD_REG_01";
	public static String INVALID_EMAILID_OR_PASSWORD="STUD_REG_02";
	public static String STUD_ALREADY_REGISTERED="STUD_REG_03";
	public static String INVALID_PWD_LEN="STUD_REG_04";
	public static String STUD_IS_INACTIVE="STUD_REG_05";
	public static String INVALID_EMAIL_ID="STUD_06";
	public static String INVALID_SECURITY_CODE="STUD_07";
	public static String INVALID_PASSWORD ="STUD_08";
	public static String INVALID_VERIFY_PWD="STUD_09";
	public static String PWD_VPWD_DONT_MATCH = "STUD_10";
	public static String SEC_CODE_VALIDATION_FAILURE = "STUD_11";
	public static String STUD_LOGIN_NOT_FOUND = "STUD_12";
	public static String APP_SEC_KEY_NOT_FOUND= "APP_SEC_01";
	public static String UNAUTH_ACCESS= "UNAUTH_ACCESS_01";
	
	
}
