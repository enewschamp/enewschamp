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

import com.enewschamp.article.app.dto.NewsArticleQuizDTO;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.domain.service.NewsArticleQuizService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class NewsArticleQuizController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private NewsArticleQuizService newsArticleQuizService;

	@PostMapping(value = "/newsArticleQuizs")
	public ResponseEntity<NewsArticleQuizDTO> create(@RequestBody @Valid NewsArticleQuizDTO newsArticleQuizDTO) {
		NewsArticleQuiz newsArticleQuiz = modelMapper.map(newsArticleQuizDTO, NewsArticleQuiz.class);
		newsArticleQuiz = newsArticleQuizService.create(newsArticleQuiz);
		newsArticleQuizDTO = modelMapper.map(newsArticleQuiz, NewsArticleQuizDTO.class);
		return new ResponseEntity<NewsArticleQuizDTO>(newsArticleQuizDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/newsArticleQuizs/{newsArticleQuizId}")
	public ResponseEntity<NewsArticleQuizDTO> update(@RequestBody @Valid NewsArticleQuizDTO newsArticleQuizDTO,
			@PathVariable Long newsArticleQuizId) {
		newsArticleQuizDTO.setNewsArticleQuizId(newsArticleQuizId);
		NewsArticleQuiz newsArticleQuiz = modelMapper.map(newsArticleQuizDTO, NewsArticleQuiz.class);
		newsArticleQuiz = newsArticleQuizService.update(newsArticleQuiz);
		newsArticleQuizDTO = modelMapper.map(newsArticleQuiz, NewsArticleQuizDTO.class);
		return new ResponseEntity<NewsArticleQuizDTO>(newsArticleQuizDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/newsArticleQuizs/{newsArticleQuizId}")
	public ResponseEntity<NewsArticleQuizDTO> patch(@RequestBody NewsArticleQuizDTO newsArticleQuizDTO,
			@PathVariable Long newsArticleQuizId) {
		newsArticleQuizDTO.setNewsArticleQuizId(newsArticleQuizId);
		NewsArticleQuiz newsArticleQuiz = modelMapper.map(newsArticleQuizDTO, NewsArticleQuiz.class);
		newsArticleQuiz = newsArticleQuizService.patch(newsArticleQuiz);
		newsArticleQuizDTO = modelMapper.map(newsArticleQuiz, NewsArticleQuizDTO.class);
		return new ResponseEntity<NewsArticleQuizDTO>(newsArticleQuizDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/newsArticleQuizs/{newsArticleQuizId}")
	public ResponseEntity<Void> delete(@PathVariable Long newsArticleQuizId) {
		newsArticleQuizService.delete(newsArticleQuizId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/newsArticleQuizs/{newsArticleQuizId}")
	public ResponseEntity<NewsArticleQuizDTO> get(@PathVariable Long newsArticleQuizId) {
		NewsArticleQuiz newsArticleQuiz = newsArticleQuizService.get(newsArticleQuizId);
		NewsArticleQuizDTO newsArticleQuizDTO = modelMapper.map(newsArticleQuiz, NewsArticleQuizDTO.class);
		newsArticleQuizService.get(newsArticleQuizId);
		return new ResponseEntity<NewsArticleQuizDTO>(newsArticleQuizDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/newsArticleQuizs/{newsArticleQuizId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long newsArticleQuizId) {
		String audit = newsArticleQuizService.getAudit(newsArticleQuizId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
