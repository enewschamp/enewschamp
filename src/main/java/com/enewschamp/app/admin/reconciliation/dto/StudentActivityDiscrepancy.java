package com.enewschamp.app.admin.reconciliation.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentActivityDiscrepancy {
	private List<NewsArticleInfo> newsArticles;
	private Long studentId;
	// private StudentInfo student;

}
