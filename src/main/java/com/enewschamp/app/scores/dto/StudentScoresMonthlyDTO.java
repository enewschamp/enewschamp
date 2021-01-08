package com.enewschamp.app.scores.dto;

import java.io.Serializable;
import java.util.List;

import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;

import lombok.Data;

@Data
public class StudentScoresMonthlyDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String month;
	private List<StudentScoresMonthlyGenreDTO> byGenre;
	private List<StudentScoresMonthlyTotalDTO> total;
}
