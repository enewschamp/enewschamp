package com.enewschamp.app.champs.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.student.dto.ChampStudentDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChampsPageData extends PageData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String readingLevel;
	private String monthYear;
	private List<ChampStudentDTO> champs;
	
}
