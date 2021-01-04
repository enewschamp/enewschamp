package com.enewschamp.subscription.app.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageDTO;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class SubscriptionController {

	@PostMapping(value = "/page/subscription/next")
	public ResponseEntity<StudentSubscriptionDTO> next(@RequestBody @Valid StudentSubscriptionPageDTO subscriptionDto) {
		// StudentSubscription subscription = modelMapper.map(subscriptionDto,
		// StudentSubscription.class);
		// subscription = studentSubscriptionService.create(subscription);
		// subscriptionDto = modelMapper.map(subscription,
		// StudentSubscriptionDTO.class);
		// return new ResponseEntity<StudentSubscriptionDTO>(subscriptionDto,
		// HttpStatus.CREATED);
		return null;
	}
}
