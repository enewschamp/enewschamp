package com.enewschamp.article.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.article.domain.entity.NewsArticleQuiz;

@JaversSpringDataAuditable
interface NewsArticleQuizRepository extends JpaRepository<NewsArticleQuiz, Long>{ 
	 
} 