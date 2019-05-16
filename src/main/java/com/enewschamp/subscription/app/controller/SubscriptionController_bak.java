package com.enewschamp.subscription.app.controller;

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

import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domin.entity.StudentSubscription;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1/bak")
public class SubscriptionController_bak {
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private StudentSubscriptionService studentSubscriptionService;

	@PostMapping(value = "/page/subscription/next")
	public ResponseEntity<StudentSubscriptionDTO> next(@RequestBody @Valid StudentSubscriptionPageDTO subscriptionDto) {
		//StudentSubscription subscription = modelMapper.map(subscriptionDto, StudentSubscription.class);
		//subscription = studentSubscriptionService.create(subscription);
		//subscriptionDto = modelMapper.map(subscription, StudentSubscriptionDTO.class);
		//return new ResponseEntity<StudentSubscriptionDTO>(subscriptionDto, HttpStatus.CREATED);
		return null;
	}

	
	@PostMapping(value = "/studentsubscriptions")
	public ResponseEntity<StudentSubscriptionDTO> create(@RequestBody @Valid StudentSubscriptionDTO subscriptionDto) {
		StudentSubscription subscription = modelMapper.map(subscriptionDto, StudentSubscription.class);
		subscription = studentSubscriptionService.create(subscription);
		subscriptionDto = modelMapper.map(subscription, StudentSubscriptionDTO.class);
		return new ResponseEntity<StudentSubscriptionDTO>(subscriptionDto, HttpStatus.CREATED);
	}

	@PutMapping(value = "/studentsubscriptions/{subscriptionId}")
	public ResponseEntity<StudentSubscriptionDTO> update(@RequestBody @Valid StudentSubscriptionDTO StudentSubscriptionDTO, @PathVariable Long studentID) {
		StudentSubscriptionDTO.setStudentID(studentID);
		StudentSubscription StudentSubscription = modelMapper.map(StudentSubscriptionDTO, StudentSubscription.class);
		StudentSubscription = studentSubscriptionService.update(StudentSubscription);
		StudentSubscriptionDTO = modelMapper.map(StudentSubscription, StudentSubscriptionDTO.class);
		return new ResponseEntity<StudentSubscriptionDTO>(StudentSubscriptionDTO, HttpStatus.OK);
	}
	
	@PatchMapping(value = "/studentsubscriptions/{studentId}")
	public ResponseEntity<StudentSubscriptionDTO> patch(@RequestBody StudentSubscriptionDTO StudentSubscriptionDTO, @PathVariable Long studentId) {
		StudentSubscriptionDTO.setStudentID(studentId);
		StudentSubscription StudentSubscription = modelMapper.map(StudentSubscriptionDTO, StudentSubscription.class);
		StudentSubscription = studentSubscriptionService.patch(StudentSubscription);
		StudentSubscriptionDTO = modelMapper.map(StudentSubscription, StudentSubscriptionDTO.class);
		return new ResponseEntity<StudentSubscriptionDTO>(StudentSubscriptionDTO, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/studentsubscriptions/{StudentSubscriptionId}")
	public ResponseEntity<Void> delete(@PathVariable Long studentId, @PathVariable Long editionId) {
		//studentSubscriptionService.delete(studentId,editionId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/studentsubscriptions/{StudentSubscriptionId}")
	public ResponseEntity<StudentSubscriptionDTO> get(@PathVariable Long studentId,@PathVariable Long editionId) {
		StudentSubscription StudentSubscription = studentSubscriptionService.get(studentId, editionId);
		StudentSubscriptionDTO StudentSubscriptionDTO = modelMapper.map(StudentSubscription, StudentSubscriptionDTO.class);
		studentSubscriptionService.get(studentId, editionId);
		return new ResponseEntity<StudentSubscriptionDTO>(StudentSubscriptionDTO, HttpStatus.OK);
	}
	@GetMapping(value = "/studentsubscriptions/{StudentSubscriptionId}/audit")
	public ResponseEntity<String> getAudit(@PathVariable Long studentId, @PathVariable Long editionId) {
		//String audit = studentSubscriptionService.getAudit(studentId, editionId);
		//return new ResponseEntity<String>(audit, HttpStatus.OK);
		return null;
	}
	
	
	
	
}
