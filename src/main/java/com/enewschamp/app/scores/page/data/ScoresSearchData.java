package com.enewschamp.app.scores.page.data;

import lombok.Data;

@Data
public class ScoresSearchData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yearMonth;
	private Long studentId;
	private int readingLevel;
}
