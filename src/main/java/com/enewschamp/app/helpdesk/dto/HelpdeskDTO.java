package com.enewschamp.app.helpdesk.dto;

import java.time.LocalDateTime;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelpdeskDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long helpdeskId;
	private Long studentId;
	private String editionId;
	private String categoryId;
	private String details;
	private String phoneNumber;
	private String supportUserId;
	private String supportComments;
	private String closeFlag;
	private LocalDateTime callbackDateTime;
	private LocalDateTime createDateTime;

}
