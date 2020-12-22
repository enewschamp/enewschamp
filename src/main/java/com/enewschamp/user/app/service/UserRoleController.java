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
import com.enewschamp.user.app.dto.UserRoleKeyDTO;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;
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

	@PostMapping(value = "/admin/users/{userId}/roles")
	public ResponseEntity<UserRoleDTO> create(@RequestBody @Valid UserRoleDTO userRoleDTO,
			@PathVariable String userId) {
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.create(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/admin/users/{userId}/roles/{roleId}")
	public ResponseEntity<UserRoleDTO> update(@RequestBody @Valid UserRoleDTO userRoleDTO, @PathVariable String userId,
			@PathVariable String roleId) {
		UserRoleKeyDTO userRoleKeyDTO = new UserRoleKeyDTO();
		userRoleKeyDTO.setUserId(userId);
		userRoleKeyDTO.setRoleId(roleId);
		userRoleDTO.setUserRoleKey(userRoleKeyDTO);
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.update(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@PatchMapping(value = "/admin/users/{userId}/roles/{roleId}")
	public ResponseEntity<UserRoleDTO> patch(@RequestBody @Valid UserRoleDTO userRoleDTO, @PathVariable String userId,
			@PathVariable String roleId) {

		UserRoleKeyDTO userRoleKeyDTO = new UserRoleKeyDTO();
		userRoleKeyDTO.setUserId(userId);
		userRoleKeyDTO.setRoleId(roleId);
		userRoleDTO.setUserRoleKey(userRoleKeyDTO);
		UserRole userRole = modelMapper.map(userRoleDTO, UserRole.class);
		userRole = userRoleService.patch(userRole);
		userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@DeleteMapping(value = "/admin/users/{userId}/roles/{roleId}")
	public ResponseEntity<Void> delete(@PathVariable String userId, @PathVariable String roleId) {
		UserRoleKeyDTO userRoleKeyDTO = new UserRoleKeyDTO();
		userRoleKeyDTO.setUserId(userId);
		userRoleKeyDTO.setRoleId(roleId);
		UserRoleKey userRoleKey = modelMapper.map(userRoleKeyDTO, UserRoleKey.class);
		userRoleService.delete(userRoleKey);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping(value = "/admin/users/{userId}/roles/{roleId}")
	public ResponseEntity<UserRoleDTO> get(@PathVariable String userId, @PathVariable String roleId) {
		UserRoleKeyDTO userRoleKeyDTO = new UserRoleKeyDTO();
		userRoleKeyDTO.setUserId(userId);
		userRoleKeyDTO.setRoleId(roleId);
		UserRoleKey userRoleKey = modelMapper.map(userRoleKeyDTO, UserRoleKey.class);
		UserRole userRole = userRoleService.get(userRoleKey);
		UserRoleDTO userRoleDTO = modelMapper.map(userRole, UserRoleDTO.class);
		return new ResponseEntity<UserRoleDTO>(userRoleDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/admin/users/{userId}/roles/{roleId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String userId, @PathVariable String roleId) {
		UserRoleKeyDTO userRoleKeyDTO = new UserRoleKeyDTO();
		userRoleKeyDTO.setUserId(userId);
		userRoleKeyDTO.setRoleId(roleId);
		UserRoleKey userRoleKey = modelMapper.map(userRoleKeyDTO, UserRoleKey.class);
		String audit = userRoleService.getAudit(userRoleKey);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}

}
