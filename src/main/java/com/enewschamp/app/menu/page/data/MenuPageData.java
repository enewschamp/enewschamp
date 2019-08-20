package com.enewschamp.app.menu.page.data;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MenuPageData extends PageData{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String premiumSubsMsg;
	private String name;
	private String surname;
	private String picImage;
}
