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

import com.enewschamp.publication.app.dto.EditionDTO;
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.service.EditionService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class EditionController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private EditionService editionService;

	@PostMapping(value = "/admin/editions")
	public ResponseEntity<EditionDTO> create(@RequestBody @Valid EditionDTO editionDTO) {
		Edition edition = modelMapper.map(editionDTO, Edition.class);
		edition = editionService.create(edition);
		editionDTO = modelMapper.map(edition, EditionDTO.class);
		return new ResponseEntity<EditionDTO>(editionDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/editions/{editionId}")
	public ResponseEntity<EditionDTO> update(@RequestBody @Valid EditionDTO editionDTO,
			@PathVariable String editionId) {
		editionDTO.setEditionId(editionId);
		Edition edition = modelMapper.map(editionDTO, Edition.class);
		edition = editionService.update(edition);
		editionDTO = modelMapper.map(edition, EditionDTO.class);
		return new ResponseEntity<EditionDTO>(editionDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/editions/{editionId}")
	public ResponseEntity<EditionDTO> patch(@RequestBody EditionDTO editionDTO, @PathVariable String editionId) {
		editionDTO.setEditionId(editionId);
		Edition edition = modelMapper.map(editionDTO, Edition.class);
		edition = editionService.patch(edition);
		editionDTO = modelMapper.map(edition, EditionDTO.class);
		return new ResponseEntity<EditionDTO>(editionDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/editions/{editionId}")
	public ResponseEntity<Void> delete(@PathVariable String editionId) {
		editionService.delete(editionId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/editions/{editionId}")
	public ResponseEntity<EditionDTO> get(@PathVariable String editionId) {
		Edition edition = editionService.get(editionId);
		EditionDTO editionDTO = modelMapper.map(edition, EditionDTO.class);
		editionService.get(editionId);
		return new ResponseEntity<EditionDTO>(editionDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/editions/{editionId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String editionId) {
		String audit = editionService.getAudit(editionId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
