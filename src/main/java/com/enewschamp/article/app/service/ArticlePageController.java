//package com.enewschamp.article.app.service;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.enewschamp.app.service.PageController;
//import com.enewschamp.article.domain.service.NewsArticleSearchPageBuilder;
//import com.enewschamp.article.page.dto.NewsArticleSearchPageDTO;
//import com.enewschamp.domain.common.PageBuilderFactory;
//
//import lombok.extern.java.Log;
//
//@Log
//@RestController
//@RequestMapping("/enewschamp-api/v1")
//public class ArticlePageController extends PageController {
//
//	@Autowired
//	ModelMapper modelMapper;
//	
////	@Autowired
////	private NewsArticlePageBuilder newsArticlePageBuilder;
//
//	@Autowired
//	private PageBuilderFactory pageBuilderFactory;
//
//	
//	@GetMapping(value = "/pages/{pageName}/{actionName}")
//	public ResponseEntity<NewsArticleSearchPageDTO> get(@PathVariable String pageName, @PathVariable String actionName) {
//		
//		NewsArticleSearchPageDTO page = (NewsArticleSearchPageDTO) pageBuilderFactory.getPageBuilder(pageName, actionName).buildPage();
//		
//		//NewsArticleSearchPageDTO page = newsArticlePageService.buildPage();
//		return new ResponseEntity<NewsArticleSearchPageDTO>(page, HttpStatus.OK);
//	}
//	
//
//}
