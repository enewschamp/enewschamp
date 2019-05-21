package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentPreferencePageDTO;
import com.enewschamp.subscription.domain.entity.StudentPreference;
import com.enewschamp.subscription.domain.service.StudentPreferenceService;

@Service
public class PreferenceBusiness {
	
	@Autowired
	ModelMapper modelMapper; 
	
	@Autowired
	StudentPreferenceService  studentPreferenceService;
	
	public void saveAsMaster(StudentPreferencePageDTO studentPreferencePageDTO)
	{
		StudentPreference studentPref = modelMapper.map(studentPreferencePageDTO.getData(),StudentPreference.class);
		studentPreferenceService.create(studentPref);
	}
}
