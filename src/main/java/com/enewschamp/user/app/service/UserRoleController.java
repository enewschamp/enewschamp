package com.enewschamp.user.app.service;

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

import com.enewschamp.user.app.dto.UserRoleDTO;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.service.UserRoleService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class UserRoleController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private UserRoleService userRoleService;

	@PostMapping(value = "/admin/userroles")
	public ResponseEntity<UserRoleDTO> create(@RequestBody @Valid UserRoleDTO userRoleDTO) {
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.create(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/userroles/{userRoleId}")
	public ResponseEntity<UserRoleDTO> update(@RequestBody @Valid UserRoleDTO userRoleDTO,
			@PathVariable Long userRoleId) {
		userRoleDTO.setUserRoleId(userRoleDTO.getUserRoleId());
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.update(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/userroles/{userRoleId}")
	public ResponseEntity<UserRoleDTO> patch(@RequestBody @Valid UserRoleDTO userRoleDTO,
			@PathVariable Long userRoleId) {
		userRoleDTO.setUserRoleId(userRoleId);
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.patch(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/userroles/{userRoleId}")
	public ResponseEntity<Void> delete(@PathVariable Long userRoleId) {
		userRoleService.delete(userRoleId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/userroles/{userRoleId}")
	public ResponseEntity<UserRoleDTO> get(@PathVariable Long userRoleId) {
		UserRole userRole = userRoleService.get(userRoleId);
		UserRoleDTO userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/userroles/{userRoleId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long userRoleId) {
		String audit = userRoleService.getAudit(userRoleId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
