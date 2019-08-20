package com.enewschamp.app.helpdesk.dto;

import java.time.LocalDateTime;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HelpDeskDTO extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long requestId;
	private Long studentId;
	private String editionId;
	private String categoryId;
	private String details;
	private String callbackRequest;
	private Long phoneNumber;
	private String supportingComments;
	private String closeFlag;
	private LocalDateTime callBackTime;
	private LocalDateTime createDateTime;
	
}
