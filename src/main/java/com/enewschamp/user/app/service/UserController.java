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

import com.enewschamp.user.app.dto.UserDTO;
import com.enewschamp.user.domain.entity.User;
import com.enewschamp.user.domain.service.UserService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class UserController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;

	@PostMapping(value = "/users")
	public ResponseEntity<UserDTO> create(@RequestBody @Valid UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		user = userService.create(user);
		userDTO = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/users/{userId}")
	public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO userDTO, @PathVariable String userId) {
		userDTO.setUserId(userId);
		User user = modelMapper.map(userDTO, User.class);
		user = userService.update(user);
		userDTO = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/users/{userId}")
	public ResponseEntity<UserDTO> patch(@RequestBody UserDTO userDTO, @PathVariable String userId) {
		userDTO.setUserId(userId);
		User user = modelMapper.map(userDTO, User.class);
		user = userService.patch(user);
		userDTO = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/users/{userId}")
	public ResponseEntity<Void> delete(@PathVariable String userId) {
		userService.delete(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{userId}")
	public ResponseEntity<UserDTO> get(@PathVariable String userId) {
		User user = userService.load(userId);
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		user = userService.load(userId);
		userDTO = modelMapper.map(user, UserDTO.class);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{userId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String userId) {
		String audit = userService.getAudit(userId);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
