package com.enewschamp.app.about.page.data;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AboutUsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aboutUsText;
}
