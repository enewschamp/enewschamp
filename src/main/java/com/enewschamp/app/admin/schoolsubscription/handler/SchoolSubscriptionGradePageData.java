package com.enewschamp.app.admin.schoolsubscription.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolSubscriptionGradePageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long schoolSubsGradeId;
	private String schoolId;
	private String editionId;
	private String grade;
	private String section;
	private String comments;

}
