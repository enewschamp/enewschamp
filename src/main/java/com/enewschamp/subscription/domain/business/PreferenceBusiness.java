package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.enewschamp.subscription.app.dto.StudentPreferencePageDTO;
import com.enewschamp.subscription.domain.service.StudentPreferenceService;
import com.enewschamp.subscription.domin.entity.StudentPreference;

public class PreferenceBusiness {
	
	@Autowired
	ModelMapper mapper; 
	
	@Autowired
	StudentPreferenceService  studentPreferenceService;
	
	public void saveAsMaster(StudentPreferencePageDTO studentPreferencePageDTO)
	{
		StudentPreference studentPref = mapper.map(studentPreferencePageDTO.getData(),StudentPreference.class);
		studentPreferenceService.create(studentPref);
	}
}
