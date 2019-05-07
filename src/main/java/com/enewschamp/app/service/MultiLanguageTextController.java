package com.enewschamp.app.service;

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

import com.enewschamp.domain.entity.MultiLanguageText;
import com.enewschamp.domain.service.MultiLanguageTextService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class MultiLanguageTextController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private MultiLanguageTextService multiLanguageTextService;

	@PostMapping(value = "/multiLanguageTexts")
	public ResponseEntity<MultiLanguageTextDTO> create(@RequestBody @Valid MultiLanguageTextDTO multiLanguageTextDTO) {
		MultiLanguageText multiLanguageText = modelMapper.map(multiLanguageTextDTO, MultiLanguageText.class);
		multiLanguageText = multiLanguageTextService.create(multiLanguageText);
		multiLanguageTextDTO = modelMapper.map(multiLanguageText, MultiLanguageTextDTO.class);
		return new ResponseEntity<MultiLanguageTextDTO>(multiLanguageTextDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/multiLanguageTexts/{multiLanguageTextId}")
	public ResponseEntity<MultiLanguageTextDTO> update(@RequestBody @Valid MultiLanguageTextDTO multiLanguageTextDTO, @PathVariable Long multiLanguageTextId) {
		multiLanguageTextDTO.setMultiLanguageTextId(multiLanguageTextId);
		MultiLanguageText multiLanguageText = modelMapper.map(multiLanguageTextDTO, MultiLanguageText.class);
		multiLanguageText = multiLanguageTextService.update(multiLanguageText);
		multiLanguageTextDTO = modelMapper.map(multiLanguageText, MultiLanguageTextDTO.class);
		return new ResponseEntity<MultiLanguageTextDTO>(multiLanguageTextDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/multiLanguageTexts/{multiLanguageTextId}")
	public ResponseEntity<MultiLanguageTextDTO> patch(@RequestBody MultiLanguageTextDTO multiLanguageTextDTO, @PathVariable Long multiLanguageTextId) {
		multiLanguageTextDTO.setMultiLanguageTextId(multiLanguageTextId);
		MultiLanguageText multiLanguageText = modelMapper.map(multiLanguageTextDTO, MultiLanguageText.class);
		multiLanguageText = multiLanguageTextService.patch(multiLanguageText);
		multiLanguageTextDTO = modelMapper.map(multiLanguageText, MultiLanguageTextDTO.class);
		return new ResponseEntity<MultiLanguageTextDTO>(multiLanguageTextDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/multiLanguageTexts/{multiLanguageTextId}")
	public ResponseEntity<Void> delete(@PathVariable Long multiLanguageTextId) {
		multiLanguageTextService.delete(multiLanguageTextId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/multiLanguageTexts/{multiLanguageTextId}")
	public ResponseEntity<MultiLanguageTextDTO> get(@PathVariable Long multiLanguageTextId) {
		MultiLanguageText multiLanguageText = multiLanguageTextService.get(multiLanguageTextId);
		MultiLanguageTextDTO multiLanguageTextDTO = modelMapper.map(multiLanguageText, MultiLanguageTextDTO.class);
		multiLanguageTextService.get(multiLanguageTextId);
		return new ResponseEntity<MultiLanguageTextDTO>(multiLanguageTextDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/multiLanguageTexts/{multiLanguageTextId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long multiLanguageTextId) {
		String audit = multiLanguageTextService.getAudit(multiLanguageTextId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
