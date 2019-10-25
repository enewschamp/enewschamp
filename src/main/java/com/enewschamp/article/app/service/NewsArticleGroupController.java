package com.enewschamp.article.app.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;

import lombok.var;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class NewsArticleGroupController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private NewsArticleGroupService newsArticleGroupService;
	
	@Autowired
	private NewsArticleGroupHelper newsArticleGroupHelper;

	@PostMapping(value = "/articleGroups")
	public ResponseEntity<NewsArticleGroupDTO> createArticleGroup(@RequestBody @Valid NewsArticleGroupDTO articleGroupDTO) {
		articleGroupDTO = newsArticleGroupHelper.createArticleGroup(articleGroupDTO);
		return new ResponseEntity<NewsArticleGroupDTO>(articleGroupDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/articleGroups/{articleGroupId}")
	public ResponseEntity<NewsArticleGroupDTO> updateArticleGroup(@RequestBody @Valid NewsArticleGroupDTO articleGroupDTO, @PathVariable Long articleGroupId) {
		articleGroupDTO.setNewsArticleGroupId(articleGroupId);
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.update(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		return new ResponseEntity<NewsArticleGroupDTO>(articleGroupDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/articleGroups/{articleGroupId}")
	public ResponseEntity<NewsArticleGroupDTO> patchArticleGroup(@RequestBody NewsArticleGroupDTO articleGroupDTO, @PathVariable Long articleGroupId) {
		articleGroupDTO.setNewsArticleGroupId(articleGroupId);
		NewsArticleGroup articleGroup = modelMapper.map(articleGroupDTO, NewsArticleGroup.class);
		articleGroup = newsArticleGroupService.patch(articleGroup);
		articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		return new ResponseEntity<NewsArticleGroupDTO>(articleGroupDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/articleGroups/{articleGroupId}")
	public ResponseEntity<Void> deleteArticleGroup(@PathVariable Long articleGroupId) {
		newsArticleGroupService.delete(articleGroupId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/articleGroups/{articleGroupId}")
	public ResponseEntity<NewsArticleGroupDTO> getArticleGroup(@PathVariable Long articleGroupId) {
		NewsArticleGroup articleGroup = newsArticleGroupService.get(articleGroupId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		newsArticleGroupService.get(articleGroupId);
		return new ResponseEntity<NewsArticleGroupDTO>(articleGroupDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/articleGroups/{articleGroupId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long articleGroupId) {
		String audit = newsArticleGroupService.getAudit(articleGroupId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	@GetMapping(value = "/articleGroups/{articleGroupId}/audit/comments")
	public ResponseEntity<String> getCommentsAudit(@PathVariable Long articleGroupId) {
		String audit = newsArticleGroupService.getCommentsAudit(articleGroupId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
