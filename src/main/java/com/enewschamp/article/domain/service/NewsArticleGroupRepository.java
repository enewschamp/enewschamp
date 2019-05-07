package com.enewschamp.article.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.article.domain.entity.NewsArticleGroup;

@JaversSpringDataAuditable
public interface NewsArticleGroupRepository extends JpaRepository<NewsArticleGroup, Long>{ 
	 
} 