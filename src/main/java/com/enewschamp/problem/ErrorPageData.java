package com.enewschamp.problem;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.domain.service.ValidationError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse" })
public class ErrorPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String errorCode;

	@JsonInclude
	private String errorMessage;

	@JsonIgnore
	private List<ValidationError> validationErrors;

	@JsonIgnore
	private String[] errorMessageParams;

}
