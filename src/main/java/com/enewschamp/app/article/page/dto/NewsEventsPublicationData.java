package com.enewschamp.app.article.page.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NewsEventsPublicationData {

	private Long newsArticleID;
	private String image;
	private String city;
	private LocalDate publishDate;
	
	
}
