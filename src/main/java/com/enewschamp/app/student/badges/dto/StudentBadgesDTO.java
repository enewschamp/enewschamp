package com.enewschamp.app.student.badges.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentBadgesDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long studentBadgesId;
	private Long studentId;
	private Long badgeId;
	private Long yearMonth;
}
