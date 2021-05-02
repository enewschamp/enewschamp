package com.enewschamp.domain.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.IOUtils;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.problem.BusinessException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

public interface IPageHandler {

	@Slf4j
	final class LogHolder {
	}

	// Method to process the data based on action take on the page
	public PageDTO handleAction(PageRequestDTO pageRequest);

	public PageDTO loadPage(PageNavigationContext pageNavigationContext);

	// save to master
	public PageDTO saveAsMaster(PageRequestDTO pageRequest);

	// method to handle actions from app
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO);

	default void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
	}

	default void validate(Object pageData, String className) {
		Validator validator;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				LogHolder.log.error("Validation failed for caller class " + className + " for an object "
						+ pageData.getClass() + ": " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}

	@SneakyThrows
	default String blobToString(Blob blobData) throws SQLException, IOException {
		InputStream bstream = blobData.getBinaryStream();
		return IOUtils.toString(bstream, "UTF-8");
	}

	default Blob stringToBlob(String data) throws SerialException, SQLException {
		if (data == null) {
			return null;
		}
		Blob blob = new SerialBlob(data.getBytes());
		return blob;
	}

}