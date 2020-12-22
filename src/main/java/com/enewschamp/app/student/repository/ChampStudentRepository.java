package com.enewschamp.app.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.student.dto.ChampStudentDTO;

public interface ChampStudentRepository {

	public Page<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, Pageable pageable);

	public Page<ChampStudentDTO> findChampions(ChampsSearchData searchRequest, Pageable pageable);

}
