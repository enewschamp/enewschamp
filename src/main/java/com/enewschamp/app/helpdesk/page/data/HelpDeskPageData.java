package com.enewschamp.app.helpdesk.page.data;

import java.time.LocalDate;
import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.page.dto.ListOfValuesItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HelpDeskPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String incompleteFormText;
	private List<LocalDate> next7WorkingDays;
	private Long workinghHoursFrom;
	private Long workingHoursTo;
	private String emailId;
	private String helpDeskText;
	private List<ListOfValuesItem> categoryLOV;
private String studentMobile;

	
}
