package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentShareAchievementsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private String personalisedMessage;

	@JsonInclude
	private String approvalRequired;

	@JsonInclude
	private List<ReceipientsPageData> recipients;

}
