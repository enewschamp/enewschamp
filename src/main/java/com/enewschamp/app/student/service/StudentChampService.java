package com.enewschamp.app.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.publication.champs.repository.StudentChampsRepositoryCustomImpl;
import com.enewschamp.app.champs.entity.Champ;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.repository.ChampRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentChampService {
	private final ChampRepositoryImpl repository;
	private final StudentChampsRepositoryCustomImpl repositoryCustom;

	public Page<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		if (searchRequest.getYearMonth() != null && (searchRequest.getYearMonth().length() == 4)) {
			return repository.findYearlyChampions(searchRequest, pageable);
		} else if (searchRequest.getYearMonth() != null
				&& (searchRequest.getYearMonth().substring(4, 5).equalsIgnoreCase("Q"))) {
			return repository.findQuarterlyChampions(searchRequest, pageable);
		} else {
			return repository.findChampions(searchRequest, pageable);
		}
	}
	
	public Page<Champ> listStudentChamps(AdminSearchRequest searchRequest,
			int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Champ> champList = repositoryCustom.findAll(pageable, searchRequest);
		return champList;
	}
}
