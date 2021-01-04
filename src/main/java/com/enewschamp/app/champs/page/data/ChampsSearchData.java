package com.enewschamp.app.champs.page.data;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ChampsSearchData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String readingLevel;
	private String yearMonth;
	private LocalDate limitDate;
}
