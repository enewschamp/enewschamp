package com.enewschamp.problem;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.Violation;

import com.enewschamp.common.domain.service.ErrorCodesService;

@ControllerAdvice
public class ExceptionHandling implements ProblemHandling, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	ErrorCodesService errorCodesService;

	public Violation createViolation(final FieldError error) {
		final String fieldName = formatFieldName(error.getField());
		return new Violation(fieldName, applicationContext.getMessage(error, Locale.US));
	}

	public Violation createViolation(final ObjectError error) {
		final String fieldName = formatFieldName(error.getObjectName());
		return new Violation(fieldName, applicationContext.getMessage(error, Locale.US));
	}

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public URI defaultConstraintViolationType() {
		return URI.create("https://com.enewschamp.api/problem/constraint-violation");
	}

	public Optional<MediaType> negotiate(final NativeWebRequest request) {
		// ProblemHandling only works with JSON, not with XML
		Optional<MediaType> contentType = ProblemHandling.super.negotiate(request);
		String[] headerValueArray = request.getHeaderValues(HttpHeaders.ACCEPT);

		if (headerValueArray != null) {
			// if the client specify accepts type application/xml or application/problem+xml
			List<String> headerValues = Arrays.asList(headerValueArray);
			List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerValues);
			if (mediaTypes.stream()
					.filter(m -> (((m.isCompatibleWith(MediaType.APPLICATION_XML)
							|| m.isCompatibleWith(MediaType.APPLICATION_PROBLEM_XML)) && (!m.equals(MediaType.ALL)))))
					.findFirst().orElse(null) != null) {
				// the client accepts application/xml or application/problem+xml
				// returning an empty calls the fallback method
				// return Optional.of(MediaType.APPLICATION_PROBLEM_XML);
				return Optional.of(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
			}
		}
		return contentType;
	}

	public ResponseEntity<Problem> process(final ResponseEntity<Problem> entity,
			@SuppressWarnings("UnusedParameters") final NativeWebRequest request) {
		return process(entity);
	}

	public ResponseEntity<Problem> process(final ResponseEntity<Problem> entity) {

		if (entity.getBody() != null && entity.getBody() instanceof Fault) {
			Fault fault = (Fault) entity.getBody();
			setErrorMessages(fault);
		}
		return entity;
	}

	private void setErrorMessages(Fault fault) {
		if (fault.getData().getErrorCode() != null) {
			fault.getData().setErrorMessage(
					buildErrorMessage(fault.getData().getErrorCode(), fault.getData().getErrorMessageParams()));
		}
		fault.getData().setErrorMessageParams(null);
		if (fault.getData().getValidationErrors() != null) {
			fault.getData().getValidationErrors().forEach(validationError -> {
				validationError.setErrorMessage(errorCodesService.getValue(validationError.getErrorCode()));
			});
		}
		fault.getData().setValidationErrors(null);
		fault.setStatus(null);
	}

	private String buildErrorMessage(String errorCode, String[] errorMessageParams) {
		String errorMessage = "";
		if (errorCode != null) {
			String msgString = errorCodesService.getValue(errorCode);
			if (msgString != null) {
				try {
					msgString = new String(msgString.getBytes("ISO-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (errorMessageParams != null) {
					errorMessage = MessageFormat.format(msgString, errorMessageParams);
				}
			}
		}
		return errorMessage;
	}

}