package com.enewschamp.domain.common;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.problem.BusinessException;

import lombok.extern.slf4j.Slf4j;
public interface IPageHandler {
	
    @Slf4j
    final class LogHolder
    {}

	// Method to process the data based on action take on the page
	public PageDTO handleAction(PageRequestDTO pageRequest);

	public PageDTO loadPage(PageNavigationContext pageNavigationContext);

	// save to master
	public PageDTO saveAsMaster(PageRequestDTO pageRequest);

	// method to handle actions from app
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO);
	
	default void validate(Object pageData) {
		Validator validator;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				 LogHolder.log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

}