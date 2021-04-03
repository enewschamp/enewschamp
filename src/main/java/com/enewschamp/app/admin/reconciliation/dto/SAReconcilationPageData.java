package com.enewschamp.app.admin.reconciliation.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SAReconcilationPageData extends PageData {
	private static final long serialVersionUID = 1L;
	List<StudentActivityDiscrepancy> listStudentActivityDiscrepancies;
	List<StudentDailyScoresDiscripancies> listStudentDailyScoresDiscrepancies;

}
