package com.enewschamp.article.domain.common;

public enum ArticleStatusType {

	Unassigned,
	Assigned,
	UnderReview,
	Rework,
	ReadyToPublish,
	Published,
	Closed;
	
	public static ArticleStatusType fromValue(String status) {
		for(ArticleStatusType statusType : ArticleStatusType.values()) {
			if(statusType.toString().equals(status)) {
				return statusType;
			}
		}
		return null;
	}
}

