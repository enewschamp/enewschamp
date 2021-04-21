package com.enewschamp.app.admin.properties.backend.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PropertiesBackendPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long propertyId;
	private String appName;

	@NotNull(message = MessageConstants.PROPERTY_NAME_NOT_NULL)
	@NotEmpty(message = MessageConstants.PROPERTY_NAME_NOT_EMPTY)
	private String name;

	@NotNull(message = MessageConstants.PROPERTY_VALUE_NOT_NULL)
	@NotEmpty(message = MessageConstants.PROPERTY_VALUE_NOT_EMPTY)
	private String value;

}
