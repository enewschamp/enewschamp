package com.enewschamp.domain.common;

import java.io.Serializable;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageNavigationContext implements Serializable {

	private static final long serialVersionUID = -714029807123011714L;

	private PageRequestDTO pageRequest;
	private PageDTO previousPageResponse;
	private String actionName;
	private String loadMethod;
}
