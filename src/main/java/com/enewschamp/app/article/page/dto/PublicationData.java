package com.enewschamp.app.article.page.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PublicationData {

	private static final long serialVersionUID = 1L;

	private Long newsArticleId;
	private String imagePathMobile;
	private boolean quizCompletedIndicator;
	
}
