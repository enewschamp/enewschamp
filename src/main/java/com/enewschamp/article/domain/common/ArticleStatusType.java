package com.enewschamp.article.domain.common;

public enum ArticleStatusType {

	Unassigned(false),
	Assigned(false),
	UnderReview(true),
	Rework(false),
	ReadyToPublish(true),
	Published(true),
	Closed(true);
	
	private boolean isLockedForEdit;
	
	private ArticleStatusType() {
		
	}
	private ArticleStatusType(boolean isLockedForEdit) {
		this.isLockedForEdit = isLockedForEdit;
	}
	
	public boolean isLockedForEdit() {
		return this.isLockedForEdit;
	}
}

