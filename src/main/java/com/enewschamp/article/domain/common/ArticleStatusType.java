package com.enewschamp.article.domain.common;

public enum ArticleStatusType {

	Initial, Unassigned, Assigned, UnderReview, ReworkForAuthor, ReworkForEditor, ReadyToPublish, Published, Closed;

	public static ArticleStatusType fromValue(String status) {
		for (ArticleStatusType statusType : ArticleStatusType.values()) {
			if (statusType.toString().equals(status)) {
				return statusType;
			}
		}
		return null;
	}
}
