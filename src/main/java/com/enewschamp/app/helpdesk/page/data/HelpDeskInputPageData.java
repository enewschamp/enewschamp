package com.enewschamp.app.helpdesk.page.data;

import java.time.LocalDate;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelpdeskInputPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String category;
	private String details;
	private String phoneNumber;
	private LocalDate preferredDate;
	private String preferredTime;

}
