package com.enewschamp.app.article.page.dto;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studentNotificationId;
	private String readFlag;

}
