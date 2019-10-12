package com.enewschamp.article.domain.common;

public enum ArticleActionType {

	SaveNewsArticleGroup, //First time creation
	AssignAuthor,
	SaveAsDraft,
	SubmitForReview,
	ReworkForAuthor,
	ReworkForEditor,
	ReadyToPublish,
	Publish,
	Close,
	Reinstate;
}
