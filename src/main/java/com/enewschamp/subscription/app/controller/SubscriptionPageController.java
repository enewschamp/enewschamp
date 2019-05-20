package com.enewschamp.subscription.app.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class SubscriptionPageController {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private StudentSubscriptionService studentSubscriptionService;
	@PostMapping(value = "/studentsubscriptions/operation/GoPremiumSubs")
	public ResponseEntity<StudentSubscriptionDTO> create(@RequestBody @Valid StudentSubscriptionDTO subscriptionDto) {
		StudentSubscription subscription = modelMapper.map(subscriptionDto, StudentSubscription.class);
		subscription = studentSubscriptionService.create(subscription);
		subscriptionDto = modelMapper.map(subscription, StudentSubscriptionDTO.class);
		return new ResponseEntity<StudentSubscriptionDTO>(subscriptionDto, HttpStatus.CREATED);
	}
}
