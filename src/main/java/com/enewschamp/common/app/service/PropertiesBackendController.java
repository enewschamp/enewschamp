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

import com.enewschamp.common.app.dto.PropertiesBackendDTO;
import com.enewschamp.common.domain.entity.PropertiesBackend;
import com.enewschamp.common.domain.service.PropertiesBackendService;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class PropertiesBackendController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PropertiesBackendService propertiesService;

	@PostMapping(value = "/admin/propertiesbackend")
	public ResponseEntity<PropertiesBackendDTO> create(@RequestBody @Valid PropertiesBackendDTO PropertiesBackendDTO) {
		PropertiesBackend properties = modelMapper.map(PropertiesBackendDTO, PropertiesBackend.class);
		properties = propertiesService.create(properties);
		PropertiesBackendDTO = modelMapper.map(properties, PropertiesBackendDTO.class);
		return new ResponseEntity<PropertiesBackendDTO>(PropertiesBackendDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/propertiesbackend/{propertiesId}")
	public ResponseEntity<PropertiesBackendDTO> update(@RequestBody @Valid PropertiesBackendDTO PropertiesBackendDTO,
			@PathVariable Long propertyId) {
		PropertiesBackendDTO.setPropertyId(propertyId);
		PropertiesBackend properties = modelMapper.map(PropertiesBackendDTO, PropertiesBackend.class);
		properties = propertiesService.update(properties);
		PropertiesBackendDTO = modelMapper.map(properties, PropertiesBackendDTO.class);
		return new ResponseEntity<PropertiesBackendDTO>(PropertiesBackendDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/propertiesbackend/{propertiesId}")
	public ResponseEntity<PropertiesBackendDTO> patch(@RequestBody PropertiesBackendDTO PropertiesBackendDTO,
			@PathVariable Long propertyId) {
		PropertiesBackendDTO.setPropertyId(propertyId);
		PropertiesBackend properties = modelMapper.map(PropertiesBackendDTO, PropertiesBackend.class);
		properties = propertiesService.patch(properties);
		PropertiesBackendDTO = modelMapper.map(properties, PropertiesBackendDTO.class);
		return new ResponseEntity<PropertiesBackendDTO>(PropertiesBackendDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/propertiesbackend/{propertiesId}")
	public ResponseEntity<Void> delete(@PathVariable Long propertiesId) {
		propertiesService.delete(propertiesId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/propertiesbackend/{propertiesId}")
	public ResponseEntity<PropertiesBackendDTO> get(@PathVariable Long propertiesId) {
		PropertiesBackend properties = propertiesService.get(propertiesId);
		PropertiesBackendDTO PropertiesBackendDTO = modelMapper.map(properties, PropertiesBackendDTO.class);
		propertiesService.get(propertiesId);
		return new ResponseEntity<PropertiesBackendDTO>(PropertiesBackendDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/propertiesbackend/{propertiesId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long propertiesId) {
		String audit = propertiesService.getAudit(propertiesId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
