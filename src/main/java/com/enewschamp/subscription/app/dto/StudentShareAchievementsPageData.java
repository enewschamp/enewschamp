package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentShareAchievementsPageData  extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value="personalisedMsg")
	private String personalMsg;
	private List<String> contacts;
 
	
}
