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

import com.enewschamp.article.app.service.NewsArticleGroupHelper;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.service.PublicationService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PublicationController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private PublicationHelper publicationHelper;

	@PostMapping(value = "/publications")
	public ResponseEntity<PublicationDTO> create(@RequestBody @Valid PublicationDTO publicationDTO) {
		publicationDTO = publicationHelper.createPublication(publicationDTO);
		return new ResponseEntity<PublicationDTO>(publicationDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/publications/{publicationId}")
	public ResponseEntity<PublicationDTO> update(@RequestBody @Valid PublicationDTO publicationDTO, @PathVariable Long publicationId) {
		publicationDTO.setPublicationId(publicationId);
		Publication publication = modelMapper.map(publicationDTO, Publication.class);
		publication = publicationService.update(publication);
		publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		return new ResponseEntity<PublicationDTO>(publicationDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/publications/{publicationId}")
	public ResponseEntity<PublicationDTO> patch(@RequestBody PublicationDTO publicationDTO, @PathVariable Long publicationId) {
		publicationDTO.setPublicationId(publicationId);
		Publication publication = modelMapper.map(publicationDTO, Publication.class);
		publication = publicationService.patch(publication);
		publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		return new ResponseEntity<PublicationDTO>(publicationDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/publications/{publicationId}")
	public ResponseEntity<Void> delete(@PathVariable Long publicationId) {
		publicationService.delete(publicationId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/publications/{publicationId}")
	public ResponseEntity<PublicationDTO> get(@PathVariable Long publicationId) {
		PublicationDTO publicationDTO = publicationHelper.getPublication(publicationId);
		return new ResponseEntity<PublicationDTO>(publicationDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/publications/{publicationId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long publicationId) {
		String audit = publicationService.getAudit(publicationId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
