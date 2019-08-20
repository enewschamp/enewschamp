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

@Service
public class StudentChampService {

	@Autowired
	ChampRepositoryImpl repository;
	
	/*public List<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest) {
	
		// TO DO.. some logic to find the top 10 students..
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ChampStudentDTO> champStudentPage = repository.findChampStudents(searchRequest, pageable);
		List<ChampStudentDTO> champStudentList = new ArrayList<ChampStudentDTO>();
		
		if(!champStudentPage.isEmpty())
		{
			champStudentList = champStudentPage.getContent();
		}
		return champStudentList;

	}*/
	public List<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest) {
		
		// TO DO.. some logic to find the top 10 students..
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ChampStudentDTO> champStudentPage = repository.findChampions(searchRequest, pageable);
		List<ChampStudentDTO> champStudentList = new ArrayList<ChampStudentDTO>();
		
		if(!champStudentPage.isEmpty())
		{
			champStudentList = champStudentPage.getContent();
		}
		return champStudentList;

	}
}
