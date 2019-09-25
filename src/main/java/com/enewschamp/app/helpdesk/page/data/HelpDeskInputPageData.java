package com.enewschamp.app.helpdesk.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HelpDeskInputPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String categoryId;
	private String details;
	private String phoneNumber;
	private LocalDate preferredDate;
	private String preferredTime;
	
	
	
}
