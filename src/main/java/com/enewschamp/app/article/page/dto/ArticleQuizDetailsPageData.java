package com.enewschamp.app.article.page.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ArticleQuizDetailsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long newsArticleId;
	private String quizFlag;
	private String incompeleteFormText;
	private List<ArticleQuizQuestionsPageData> quizQuestions;
	
	
}
