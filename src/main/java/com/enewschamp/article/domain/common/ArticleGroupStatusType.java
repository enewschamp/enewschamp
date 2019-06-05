package com.enewschamp.article.domain.common;

public enum ArticleGroupStatusType {

	Unassigned(1),
	Assigned(2),
	WIP(3),
	ReadyToPublish(4),
	Published(5),
	Closed(6);
	
	private int order;
	
	private ArticleGroupStatusType() {
		
	}
	
	private ArticleGroupStatusType(int order) {
		this.order = order;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public static ArticleGroupStatusType fromArticleStatus(ArticleStatusType articleStatus) {
		ArticleGroupStatusType status = ArticleGroupStatusType.Unassigned;
		switch(articleStatus) {
			case Assigned:
				status = ArticleGroupStatusType.Assigned;
			break;
			case UnderReview:
			case Rework:
				status = ArticleGroupStatusType.WIP;
			break;
			case ReadyToPublish:
				status = ArticleGroupStatusType.ReadyToPublish;
			break;
			case Published:
				status = ArticleGroupStatusType.Published;
			break;
			case Closed:
				status = ArticleGroupStatusType.Closed;
			break;
		}
		return status;
	}
}
