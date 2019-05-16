package com.enewschamp.article.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.article.domain.service.NewsArticlePageService;
import com.enewschamp.article.page.dto.NewsArticleSearchPage;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class ArticlePageController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticlePageService newsArticlePageService;

	@GetMapping(value = "/pages/menu")
	public ResponseEntity<NewsArticleSearchPage> get() {
		NewsArticleSearchPage page = newsArticlePageService.buildNewsArticleSearchPage();
		return new ResponseEntity<NewsArticleSearchPage>(page, HttpStatus.OK);
	}
	

}
