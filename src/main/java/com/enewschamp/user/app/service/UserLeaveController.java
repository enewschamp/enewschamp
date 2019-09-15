package com.enewschamp.user.app.service;

import java.time.LocalDate;

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

import com.enewschamp.user.app.dto.UserLeaveDTO;
import com.enewschamp.user.app.dto.UserLeaveKeyDTO;
import com.enewschamp.user.domain.entity.UserLeave;
import com.enewschamp.user.domain.entity.UserLeaveKey;
import com.enewschamp.user.domain.service.UserLeaveService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class UserLeaveController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private UserLeaveService userLeaveService;

	@PostMapping(value = "/users/{userId}/leaves")
	public ResponseEntity<UserLeaveDTO> create(@RequestBody @Valid UserLeaveDTO userLeaveDTO, @PathVariable String userId) {
		UserLeave userLeave = modelMapper.map(userLeaveDTO, UserLeave.class);
		userLeave = userLeaveService.create(userLeave);
		userLeaveDTO = modelMapper.map(userLeave, UserLeaveDTO.class);
		return new ResponseEntity<UserLeaveDTO>(userLeaveDTO, HttpStatus.CREATED);
	}

	@PutMapping(value = "/users/{userId}/leaves/{startDate}")
	public ResponseEntity<UserLeaveDTO> update(@RequestBody @Valid UserLeaveDTO userLeaveDTO, @PathVariable String userId, @PathVariable LocalDate startDate) {
		UserLeaveKeyDTO userLeaveKeyDTO = new UserLeaveKeyDTO();
		userLeaveKeyDTO.setUserId(userId);
		userLeaveKeyDTO.setStartDate(startDate);
		userLeaveDTO.setUserLeaveKey(userLeaveKeyDTO);
		UserLeave userLeave = modelMapper.map(userLeaveDTO, UserLeave.class);
		userLeave = userLeaveService.update(userLeave);
		userLeaveDTO = modelMapper.map(userLeave, UserLeaveDTO.class);
		return new ResponseEntity<UserLeaveDTO>(userLeaveDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/users/{userId}/leaves/{startDate}")
	public ResponseEntity<UserLeaveDTO> patch(@RequestBody @Valid UserLeaveDTO userLeaveDTO, @PathVariable String userId, @PathVariable LocalDate startDate) {
		
		UserLeaveKeyDTO userLeaveKeyDTO = new UserLeaveKeyDTO();
		userLeaveKeyDTO.setUserId(userId);
		userLeaveKeyDTO.setStartDate(startDate);
		userLeaveDTO.setUserLeaveKey(userLeaveKeyDTO);
		UserLeave userLeave = modelMapper.map(userLeaveDTO, UserLeave.class);
		userLeave = userLeaveService.patch(userLeave);
		userLeaveDTO = modelMapper.map(userLeave, UserLeaveDTO.class);
		return new ResponseEntity<UserLeaveDTO>(userLeaveDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/users/{userId}/leaves/{startDate}")
	public ResponseEntity<Void> delete(@PathVariable String userId, @PathVariable LocalDate startDate) {
		UserLeaveKeyDTO userLeaveKeyDTO = new UserLeaveKeyDTO();
		userLeaveKeyDTO.setUserId(userId);
		userLeaveKeyDTO.setStartDate(startDate);
		UserLeaveKey userLeaveKey = modelMapper.map(userLeaveKeyDTO, UserLeaveKey.class);
		userLeaveService.delete(userLeaveKey);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{userId}/leaves/{startDate}")
	public ResponseEntity<UserLeaveDTO> get(@PathVariable String userId, @PathVariable LocalDate startDate) {
		UserLeaveKeyDTO userLeaveKeyDTO = new UserLeaveKeyDTO();
		userLeaveKeyDTO.setUserId(userId);
		userLeaveKeyDTO.setStartDate(startDate);
		UserLeaveKey userLeaveKey = modelMapper.map(userLeaveKeyDTO, UserLeaveKey.class);
		UserLeave userLeave = userLeaveService.get(userLeaveKey);
		UserLeaveDTO userLeaveDTO = modelMapper.map(userLeave, UserLeaveDTO.class);
		return new ResponseEntity<UserLeaveDTO>(userLeaveDTO, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users/{userId}/leaves/{startDate}/audit")
	public ResponseEntity<String> getAudit(@PathVariable String userId, @PathVariable LocalDate startDate) {
		UserLeaveKeyDTO userLeaveKeyDTO = new UserLeaveKeyDTO();
		userLeaveKeyDTO.setUserId(userId);
		userLeaveKeyDTO.setStartDate(startDate);
		UserLeaveKey userLeaveKey = modelMapper.map(userLeaveKeyDTO, UserLeaveKey.class);
		String audit = userLeaveService.getAudit(userLeaveKey);
		return new ResponseEntity<String>(audit, HttpStatus.OK);
	}
	
	

}
