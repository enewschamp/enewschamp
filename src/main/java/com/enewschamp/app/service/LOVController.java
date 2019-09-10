package com.enewschamp.app.service;

import java.util.List;

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

import com.enewschamp.app.dto.LOVDTO;
import com.enewschamp.domain.entity.LOV;
import com.enewschamp.domain.service.LOVService;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class LOVController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private LOVService lovService;

	@PostMapping(value = "/listOfValues")
	public ResponseEntity<LOVDTO> create(@RequestBody @Valid LOVDTO lovDTO) {
		LOV lov = modelMapper.map(lovDTO, LOV.class);
		lov = lovService.create(lov);
		lovDTO = modelMapper.map(lov, LOVDTO.class);
		return new ResponseEntity<LOVDTO>(lovDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/listOfValues/{lovId}")
	public ResponseEntity<LOVDTO> update(@RequestBody @Valid LOVDTO lovDTO, @PathVariable Long lovId) {
		lovDTO.setLovId(lovId);
		LOV lov = modelMapper.map(lovDTO, LOV.class);
		lov = lovService.update(lov);
		lovDTO = modelMapper.map(lov, LOVDTO.class);
		return new ResponseEntity<LOVDTO>(lovDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/listOfValues/{lovId}")
	public ResponseEntity<LOVDTO> patch(@RequestBody LOVDTO lovDTO, @PathVariable Long lovId) {
		lovDTO.setLovId(lovId);
		LOV lov = modelMapper.map(lovDTO, LOV.class);
		lov = lovService.patch(lov);
		lovDTO = modelMapper.map(lov, LOVDTO.class);
		return new ResponseEntity<LOVDTO>(lovDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/listOfValues/{lovId}")
	public ResponseEntity<Void> delete(@PathVariable Long lovId) {
		lovService.delete(lovId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/listOfValues/{lovId}")
	public ResponseEntity<LOVDTO> get(@PathVariable Long lovId) {
		LOV lov = lovService.get(lovId);
		LOVDTO lovDTO = modelMapper.map(lov, LOVDTO.class);
		lovService.get(lovId);
		return new ResponseEntity<LOVDTO>(lovDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/listOfValues/{lovId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long lovId) {
		String audit = lovService.getAudit(lovId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	@GetMapping(value = "/lov/{lovType}")
	public ResponseEntity<List<ListOfValuesItem>> getLOV(@PathVariable String lovType) {
		
		return new ResponseEntity<List<ListOfValuesItem>>(lovService.getLOV(lovType), HttpStatus.OK);
	}

}
