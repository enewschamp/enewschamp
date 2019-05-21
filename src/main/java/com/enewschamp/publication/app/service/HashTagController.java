package com.enewschamp.publication.app.service;

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

import com.enewschamp.publication.app.dto.HashTagDTO;
import com.enewschamp.publication.domain.entity.HashTag;
import com.enewschamp.publication.domain.service.HashTagService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class HashTagController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private HashTagService hashTagService;

	@PostMapping(value = "/hashTags")
	public ResponseEntity<HashTagDTO> create(@RequestBody @Valid HashTagDTO hashTagDTO) {
		HashTag hashTag = modelMapper.map(hashTagDTO, HashTag.class);
		hashTag = hashTagService.create(hashTag);
		hashTagDTO = modelMapper.map(hashTag, HashTagDTO.class);
		return new ResponseEntity<HashTagDTO>(hashTagDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/hashTags/{hashTagId}")
	public ResponseEntity<HashTagDTO> update(@RequestBody @Valid HashTagDTO hashTagDTO, @PathVariable String hashTagId) {
		hashTagDTO.setHashTag(hashTagId);
		HashTag hashTag = modelMapper.map(hashTagDTO, HashTag.class);
		hashTag = hashTagService.update(hashTag);
		hashTagDTO = modelMapper.map(hashTag, HashTagDTO.class);
		return new ResponseEntity<HashTagDTO>(hashTagDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/hashTags/{hashTagId}")
	public ResponseEntity<HashTagDTO> patch(@RequestBody HashTagDTO hashTagDTO, @PathVariable String hashTagId) {
		hashTagDTO.setHashTag(hashTagId);
		HashTag hashTag = modelMapper.map(hashTagDTO, HashTag.class);
		hashTag = hashTagService.patch(hashTag);
		hashTagDTO = modelMapper.map(hashTag, HashTagDTO.class);
		return new ResponseEntity<HashTagDTO>(hashTagDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/hashTags/{hashTagId}")
	public ResponseEntity<Void> delete(@PathVariable String hashTagId) {
		hashTagService.delete(hashTagId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/hashTags/{hashTagId}")
	public ResponseEntity<HashTagDTO> get(@PathVariable String hashTagId) {
		HashTag hashTag = hashTagService.get(hashTagId);
		HashTagDTO hashTagDTO = modelMapper.map(hashTag, HashTagDTO.class);
		hashTagService.get(hashTagId);
		return new ResponseEntity<HashTagDTO>(hashTagDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/hashTags/{hashTagId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String hashTagId) {
		String audit = hashTagService.getAudit(hashTagId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
