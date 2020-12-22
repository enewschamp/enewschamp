package com.enewschamp.common.app.service;

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

import com.enewschamp.common.app.dto.PropertiesDTO;
import com.enewschamp.common.domain.entity.Properties;
import com.enewschamp.common.domain.service.PropertiesService;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class PropertiesController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PropertiesService propertiesService;

	@PostMapping(value = "/admin/properties")
	public ResponseEntity<PropertiesDTO> create(@RequestBody @Valid PropertiesDTO propertiesDTO) {
		Properties properties = modelMapper.map(propertiesDTO, Properties.class);
		properties = propertiesService.create(properties);
		propertiesDTO = modelMapper.map(properties, PropertiesDTO.class);
		return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/properties/{propertiesId}")
	public ResponseEntity<PropertiesDTO> update(@RequestBody @Valid PropertiesDTO propertiesDTO,
			@PathVariable Long propertyId) {
		propertiesDTO.setPropertyId(propertyId);
		Properties properties = modelMapper.map(propertiesDTO, Properties.class);
		properties = propertiesService.update(properties);
		propertiesDTO = modelMapper.map(properties, PropertiesDTO.class);
		return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/properties/{propertiesId}")
	public ResponseEntity<PropertiesDTO> patch(@RequestBody PropertiesDTO propertiesDTO,
			@PathVariable Long propertyId) {
		propertiesDTO.setPropertyId(propertyId);
		Properties properties = modelMapper.map(propertiesDTO, Properties.class);
		properties = propertiesService.patch(properties);
		propertiesDTO = modelMapper.map(properties, PropertiesDTO.class);
		return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/properties/{propertiesId}")
	public ResponseEntity<Void> delete(@PathVariable Long propertiesId) {
		propertiesService.delete(propertiesId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/properties/{propertiesId}")
	public ResponseEntity<PropertiesDTO> get(@PathVariable Long propertiesId) {
		Properties properties = propertiesService.get(propertiesId);
		PropertiesDTO propertiesDTO = modelMapper.map(properties, PropertiesDTO.class);
		propertiesService.get(propertiesId);
		return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/properties/{propertiesId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long propertiesId) {
		String audit = propertiesService.getAudit(propertiesId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
