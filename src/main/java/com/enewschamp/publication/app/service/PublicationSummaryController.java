package com.enewschamp.publication.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.publication.domain.service.PublicationDailySummaryService;
import com.enewschamp.publication.domain.service.PublicationMonthlySummaryService;
import com.enewschamp.publication.page.data.PublicationSummaryRequest;
import com.enewschamp.publication.page.data.PublicationSummaryResponse;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/enewschamp-api/v1")
public class PublicationSummaryController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PublicationDailySummaryService dailySummaryService;

	@Autowired
	private PublicationMonthlySummaryService monthlySummaryService;

	@GetMapping(value = "/publications/dailySummary")
	public ResponseEntity<PublicationSummaryResponse> getDailySummary(
			@RequestBody PublicationSummaryRequest summaryRequest) {
		return new ResponseEntity<PublicationSummaryResponse>(dailySummaryService.fetchSummary(summaryRequest),
				HttpStatus.OK);
	}

	@GetMapping(value = "/publications/monthlySummary")
	public ResponseEntity<PublicationSummaryResponse> getMonthlySummary(
			@RequestBody PublicationSummaryRequest summaryRequest) {
		return new ResponseEntity<PublicationSummaryResponse>(monthlySummaryService.fetchSummary(summaryRequest),
				HttpStatus.OK);
	}

}
