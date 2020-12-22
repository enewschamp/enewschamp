package com.enewschamp.app.student.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.repository.ChampRepositoryImpl;
import com.enewschamp.common.domain.service.PropertiesService;

@Service
public class StudentChampService {

	@Autowired
	ChampRepositoryImpl repository;

	@Autowired
	PropertiesService propertiesService;

	public List<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<ChampStudentDTO> champStudentPage = repository.findChampions(searchRequest, pageable);
		List<ChampStudentDTO> champStudentList = new ArrayList<ChampStudentDTO>();
		if (!champStudentPage.isEmpty()) {
			champStudentList = champStudentPage.getContent();
		}
		return champStudentList;
	}
}
