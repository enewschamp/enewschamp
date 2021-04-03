package com.enewschamp.app.common;

public interface ErrorCodeConstants {

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
	public static String INVALID_USERNAME_OR_PASSWORD = "PUB_019";
	public static String INVALID_USER_PASSWORD = "PUB_020";
	public static String INVALID_USER_NEW_PASSWORD = "PUB_021";
	public static String BOTH_PASSWORD_DO_NOT_MATCH = "PUB_022";
	public static String OLD_NEW_PASSWORD_SAME = "PUB_023";
	public static String MAX_SEARCH_LIMIT_EXCEEDED = "PUB_024";
	public static String ARTICLE_GROUP_ID_MISSING = "PUB_025";
	public static String PUBLICATION_GROUP_ID_MISSING = "PUB_026";
	public static String PUBLICATION_ALREADY_EXIST_FOR_PUB_DATE = "PUB_027";
	public static String NOT_CHANGES_FOUND = "PUB_028";
	public static String NO_CHANGES_ALLOWED = "PUB_029";
	public static String AVATAR_NOT_FOUND = "PUB_031";
	public static String BADGE_NOT_FOUND = "PUB_032";
	public static String HASHTAG_ALREADY_EXISTS = "PUB_033";
	public static String INVALID_THEME = "PUB_034";
	public static String INVALID_HASHTAG = "PUB_035";
	public static String ERROR_CODE_NOT_FOUND = "PUB_036";
	public static String PROPERTY_NOT_FOUND = "PUB_037";
	public static String IMAGE_SAVE_ERROR = "IMG_001";
	public static String FILE_SAVE_ERROR = "FILE_001";
	// Error codes for publication APIs end

	public static String STUDENT_DTLS_NOT_FOUND = "STUD_DTLS_001";

	public static String QUIZ_SCORE_NOT_FOUND = "QUIZ_001";
	public static String STUDENT_ACTIVITY_NOT_FOUND = "STUD_ACT_001";

	public static String USER_NOT_FOUND = "USER_001";
	public static String USER_ROLE_NOT_FOUND = "USER_002";
	public static String USER_LEAVE_NOT_FOUND = "USER_003";
	public static String ROLE_NOT_ASSIGNED_TO_USER = "USER_004";
	public static String USER_IS_INACTIVE = "USER_005";
	public static String INVALID_USER_ID = "USER_006";
	public static String USER_ACCOUNT_LOCKED = "USER_007";
	public static String USER_PASSWORD_SAME_OR_PREV_TWO = "USER_008";
	public static String USER_PASSWORD_FORCE_CHANGE_REQUIRED = "USER_009";
	public static String USER_ACCOUNT_GET_LOCKED = "USER_010";

	public static String PAGE_NOT_FOUND = "PAGE_001";
	public static String NEXT_PAGE_NOT_FOUND = "PAGE_002";

	public static String COUNTRY_NOT_FOUND = "COU_001";
	public static String SCHOOL_PRICING_NOT_FOUND = "SPOU_001";
	public static String STUDENT_SHARE_ACHIEVEMENTS_NOT_FOUND = "SSANF_001";

	public static String INDIVIDUAL_PRICING_NOT_FOUND = "IPOU_001";

	public static String STATE_NOT_FOUND = "STATE_001";
	public static String CITY_NOT_FOUND = "CITY_001";
	public static String SCHOOL_NOT_FOUND = "SPOU_002";
	public static String REQUESTID_NOT_FOUND = "REQ_001";

	public static String SREVER_ERROR = "OTHR_001";
	public static String RUNTIME_EXCEPTION = "OTHR_002";

	// Framework error codes start
	public static String BUS_POLICY_FAILED = "FRW_001";
	public static String STATUS_TRANSITION_NOT_FOUND = "FRW_002";
	public static String MULTI_LANG_TEXT_NOT_FOUND = "FRW_003";
	public static String LOV_NOT_FOUND = "FRW_004";
	public static String NOTIFICATION_ACTION_NOT_FOUND = "FRW_005";
	public static String ACTION_DISALLOWED_FOR_THIS_USER = "FRW_006";
	// Framework error codes end

	public static String STUD_BADGES_NOT_FOUND = "BADGE_001";

	public static String INVALID_YEARMONTH_FORMAT = "SCORE_001";
	public static String INVALID_YEAR = "SCORE_002";
	public static String INVALID_MONTH = "SCORE_003";
	public static String SCORES_DAILY_NOT_FOUND = "SCORE_004";
	public static String SCORES_MONTHLY_TOTAL_NOT_FOUND = "SCORE_005";
	public static String SCORES_MONTHLY_GENRE_NOT_FOUND = "SCORE_006";

	public static String INVALID_NOT_PRESENT = "SNA_01";
	public static String HOLIDAY_NOT_FOUND = "HOL_01";
	public static String WORKING_HOUR_NOT_FOUND = "WH_01";

	public static String MOBILE_IS_MANDATORY = "MOB_01";

	public static String NOTIFICATION_NOT_FOUND = "NOTIF_001";
	public static String STUDENT_NOTIFICATION_NOT_FOUND = "STUD_NOTIF_001";
	public static String EMAIL_NOT_SENT = "EM_NOT_01";
	public static String NO_RECORD_FOUND = "NO_REC_01";
	public static String OTP_EXPIRED = "OTP_001";
	public static String OTP_GEN_MAX_ATTEMPTS_EXHAUSTED = "OTP_002";
	public static String OTP_VERIFY_MAX_ATTEMPTS_EXHAUSTED = "OTP_003";
	public static String STUD_REG_NOT_FOUND = "STUD_REG_01";
	public static String INVALID_EMAILID_OR_PASSWORD = "LOGIN_001";
	public static String STUD_ALREADY_REGISTERED = "STUD_REG_03";
	public static String STUD_ACCOUNT_DELETED = "STUD_REG_04";
	public static String INVALID_PWD_LEN = "PASS_004";
	public static String STUD_IS_INACTIVE = "STUD_REG_05";
	public static String INVALID_EMAIL_ID = "EMAIL_001";
	public static String INVALID_SECURITY_CODE = "OTP_004";
	public static String INVALID_PASSWORD = "PASS_001";
	public static String INVALID_VERIFY_PWD = "PASS_002";
	public static String PWD_VPWD_DONT_MATCH = "PASS_003";
	public static String SEC_CODE_VALIDATION_FAILURE = "OTP_005";
	public static String STUD_LOGIN_NOT_FOUND = "STUD_LOGIN_001";
	public static String APP_SEC_KEY_NOT_FOUND = "APP_SEC_01";
	public static String APP_VERSION_NOT_SUPPORTED = "APP_SEC_02";
	public static String INVALID_REQUEST = "REQ_001";
	public static String MISSING_REQUEST_PARAMS = "REQ_002";
	public static String UNAUTH_ACCESS = "REQ_003";
	public static String USER_TOKEN_EXPIRED = "REQ_004";
	public static String PAYMENT_NOT_SUCCESSFUL = "PAY_001";
	public static String UNSUBSCRIBE_NOT_SUCCESSFUL = "SUBSCRIBE_001";
}
