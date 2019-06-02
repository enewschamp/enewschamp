package com.enewschamp.article.domain.service;

import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.publication.domain.service.AbstractBusinessPolicy;

public class ArticleBusinessPolicy extends AbstractBusinessPolicy {
	
	private NewsArticle article;
	
	public ArticleBusinessPolicy(NewsArticle article) {
		this.article = article;
	}
	
	@Override
	protected void validatePolicy() {
		validateCurrentAction(article);
	}
	
	private void validateCurrentAction(NewsArticle article) {
		if(article.getCurrentAction() == null) {
			return;
		}
		switch(article.getCurrentAction()) {
//			case SaveAsDraft:
//				if(article.getStatus().isLockedForEdit()) {
//					addValidationError(new ValidationError(ErrorCodes.INVALID_ARTICLE_ACTION, "NewsArticle.currentAction"));
//				}
//			break;
			
		}
	}
		
}
