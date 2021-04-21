package com.enewschamp.article.app.service;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class NewsArticleController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private NewsArticleHelper newsArticleHelper;

	@PostMapping(value = "/articles")
	public ResponseEntity<NewsArticleDTO> create(@RequestBody @Valid NewsArticleDTO articleDTO) {
		articleDTO = newsArticleHelper.create(articleDTO);
		return new ResponseEntity<NewsArticleDTO>(articleDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/articles/{articleId}")
	public ResponseEntity<NewsArticleDTO> update(@RequestBody @Valid NewsArticleDTO articleDTO,
			@PathVariable Long articleId) {
		articleDTO.setNewsArticleId(articleId);
		NewsArticle article = modelMapper.map(articleDTO, NewsArticle.class);
		article = newsArticleService.update(article);
		articleDTO = modelMapper.map(article, NewsArticleDTO.class);
		return new ResponseEntity<NewsArticleDTO>(articleDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/articles/{articleId}")
	public ResponseEntity<NewsArticleDTO> patch(@RequestBody NewsArticleDTO articleDTO, @PathVariable Long articleId) {
		articleDTO.setNewsArticleId(articleId);
		NewsArticle article = modelMapper.map(articleDTO, NewsArticle.class);
		article = newsArticleService.patch(article);
		articleDTO = modelMapper.map(article, NewsArticleDTO.class);
		return new ResponseEntity<NewsArticleDTO>(articleDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/articles/{articleId}")
	public ResponseEntity<Void> delete(@PathVariable Long articleId) {
		newsArticleService.delete(articleId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/articles/{articleId}")
	public ResponseEntity<NewsArticleDTO> get(@PathVariable Long articleId) {
		NewsArticleDTO articleDTO = newsArticleHelper.get(articleId);
		return new ResponseEntity<NewsArticleDTO>(articleDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/articles/{articleId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long articleId) {
		ResponseEntity<String> response = null;
		try {
			String audit = newsArticleService.getAudit(articleId);
			response = new ResponseEntity<String>(audit, HttpStatus.OK);
		} catch (BusinessException e) {
			Fault fault = new Fault(e);
			throw fault;
		}
		return response;
	}

}
