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
		validateQuiz(article);
	}

	private void validateQuiz(NewsArticle article) {
		/*
		 * if(article.getNewsArticleQuiz() != null &&
		 * article.getNewsArticleQuiz().size() > 9) { addValidationError(new
		 * ValidationError(ErrorCodes.QUIZ_LIMIT_EXCEEDED,
		 * "NewsArticle.newsArticleQuiz")); }
		 */
	}

}
