package com.enewschamp.article.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, String>{ 
	 
} 