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

import com.enewschamp.common.app.dto.PropertiesFrontendDTO;
import com.enewschamp.common.domain.entity.PropertiesFrontend;
import com.enewschamp.common.domain.service.PropertiesFrontendService;

@RestController
@RequestMapping("/enewschamp-api/v1")
public class PropertiesFrontendController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PropertiesFrontendService propertiesService;

	@PostMapping(value = "/admin/propertiesfrontend")
	public ResponseEntity<PropertiesFrontendDTO> create(
			@RequestBody @Valid PropertiesFrontendDTO PropertiesFrontendDTO) {
		PropertiesFrontend properties = modelMapper.map(PropertiesFrontendDTO, PropertiesFrontend.class);
		properties = propertiesService.create(properties);
		PropertiesFrontendDTO = modelMapper.map(properties, PropertiesFrontendDTO.class);
		return new ResponseEntity<PropertiesFrontendDTO>(PropertiesFrontendDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/propertiesfrontend/{propertiesId}")
	public ResponseEntity<PropertiesFrontendDTO> update(@RequestBody @Valid PropertiesFrontendDTO PropertiesFrontendDTO,
			@PathVariable Long propertyId) {
		PropertiesFrontendDTO.setPropertyId(propertyId);
		PropertiesFrontend properties = modelMapper.map(PropertiesFrontendDTO, PropertiesFrontend.class);
		properties = propertiesService.update(properties);
		PropertiesFrontendDTO = modelMapper.map(properties, PropertiesFrontendDTO.class);
		return new ResponseEntity<PropertiesFrontendDTO>(PropertiesFrontendDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/propertiesfrontend/{propertiesId}")
	public ResponseEntity<PropertiesFrontendDTO> patch(@RequestBody PropertiesFrontendDTO PropertiesFrontendDTO,
			@PathVariable Long propertyId) {
		PropertiesFrontendDTO.setPropertyId(propertyId);
		PropertiesFrontend properties = modelMapper.map(PropertiesFrontendDTO, PropertiesFrontend.class);
		properties = propertiesService.patch(properties);
		PropertiesFrontendDTO = modelMapper.map(properties, PropertiesFrontendDTO.class);
		return new ResponseEntity<PropertiesFrontendDTO>(PropertiesFrontendDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/propertiesfrontend/{propertiesId}")
	public ResponseEntity<Void> delete(@PathVariable Long propertiesId) {
		propertiesService.delete(propertiesId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/propertiesfrontend/{propertiesId}")
	public ResponseEntity<PropertiesFrontendDTO> get(@PathVariable Long propertiesId) {
		PropertiesFrontend properties = propertiesService.get(propertiesId);
		PropertiesFrontendDTO PropertiesFrontendDTO = modelMapper.map(properties, PropertiesFrontendDTO.class);
		propertiesService.get(propertiesId);
		return new ResponseEntity<PropertiesFrontendDTO>(PropertiesFrontendDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/propertiesfrontend/{propertiesId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long propertiesId) {
		String audit = propertiesService.getAudit(propertiesId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
