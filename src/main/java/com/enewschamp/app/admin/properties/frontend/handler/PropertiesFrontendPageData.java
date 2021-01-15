package com.enewschamp.app.admin.properties.frontend.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class PropertiesFrontendPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	private Long propertyId;
	
    @NotNull(message = MessageConstants.APP_NAME_NOT_NULL)
    @NotEmpty(message = MessageConstants.APP_NAME_NOT_EMPTY)
	private String appName;

    @NotNull(message = MessageConstants.PROPERTY_NAME_NOT_NULL)
    @NotEmpty(message = MessageConstants.PROPERTY_NAME_NOT_EMPTY)
	private String name;

    @NotNull(message = MessageConstants.PROPERTY_VALUE_NOT_NULL)
    @NotEmpty(message = MessageConstants.PROPERTY_VALUE_NOT_EMPTY)
	private String value;

}
