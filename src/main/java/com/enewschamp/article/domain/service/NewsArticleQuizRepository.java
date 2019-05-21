package com.enewschamp.article.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.article.domain.entity.NewsArticleQuiz;

@JaversSpringDataAuditable
public interface NewsArticleQuizRepository extends JpaRepository<NewsArticleQuiz, Long>{ 

	public List<NewsArticleQuiz> findByNewsArticleId(long articleId);
} 