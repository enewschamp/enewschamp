package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentPreferencesWork;
import com.enewschamp.subscription.domain.service.StudentPreferencesService;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;

@Service
public class PaymentBusiness {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentPreferencesService studentPreferencesService;

	@Autowired
	StudentPreferencesWorkService studentPreferencesWorkService;

	public void saveAsMaster(StudentPreferencesDTO studentPreferencesPageDTO) {
		StudentPreferences studentPref = modelMapper.map(studentPreferencesPageDTO, StudentPreferences.class);
		studentPreferencesService.create(studentPref);
	}

	public void saveAsWork(StudentPreferencesWorkDTO studentPreferencesWorkDTO) {
		StudentPreferencesWork studentPref = modelMapper.map(studentPreferencesWorkDTO, StudentPreferencesWork.class);
		studentPreferencesWorkService.create(studentPref);
	}

	public void workToMaster(Long studentId) {
		StudentPreferencesWork workEntity = studentPreferencesWorkService.get(studentId);
		if (workEntity != null) {
			StudentPreferences masterEntity = modelMapper.map(workEntity, StudentPreferences.class);
			studentPreferencesService.create(masterEntity);
		}
	}
}