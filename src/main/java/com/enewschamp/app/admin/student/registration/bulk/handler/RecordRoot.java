package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class RecordRoot extends PageData {
	private static final long serialVersionUID = 1L;
	List<BulkStudentRegistrationPageData2> records;

}
