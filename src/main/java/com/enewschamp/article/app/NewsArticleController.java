package com.enewschamp.article.app;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.article.domain.NewsArticle;
import com.enewschamp.article.domain.NewsArticleRepository;

import lombok.extern.java.Log; 
 
@Log 
@RestController 
@RequestMapping("/enewschamp-api/v1/") 
public class NewsArticleController { 
     
	@Autowired 
    private NewsArticleRepository newsArticleRepository; 
     
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value="/articles") 
    public NewsArticle createAPI(@RequestBody @Valid NewsArticle article) { 
        return newsArticleRepository.save(article);
    } 
     
    
} 
 