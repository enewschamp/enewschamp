package com.enewschamp.app.menu.page.data;

import com.enewschamp.app.common.PageData;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MenuPageData extends PageData {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String name;

	@JsonInclude
	private String surname;

	@JsonInclude
	private MyPicturePageData myPicture;
}
