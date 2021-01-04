package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentPreferencesWork;
import com.enewschamp.subscription.domain.service.StudentPreferencesService;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;

@Service
public class PreferenceBusiness {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentPreferencesService studentPreferencesService;

	@Autowired
	StudentPreferencesWorkService studentPreferencesWorkService;

	public void saveAsMaster(StudentPreferencesDTO studentPreferencesPageDTO) {
		StudentPreferences studentPref = modelMapper.map(studentPreferencesPageDTO, StudentPreferences.class);
		studentPref.setRecordInUse(RecordInUseType.Y);
		studentPref.setOperatorId("SYSTEM");
		studentPreferencesService.create(studentPref);
	}

	public void saveAsWork(StudentPreferencesWorkDTO studentPreferencesWorkDTO) {
		StudentPreferencesWork studentPref = modelMapper.map(studentPreferencesWorkDTO, StudentPreferencesWork.class);
		studentPref.setRecordInUse(RecordInUseType.Y);
		studentPref.setOperatorId("SYSTEM");
		studentPreferencesWorkService.create(studentPref);
	}

	public void workToMaster(Long studentId) {
		StudentPreferencesWork workEntity = studentPreferencesWorkService.get(studentId);
		if (workEntity != null) {
			StudentPreferences masterEntity = modelMapper.map(workEntity, StudentPreferences.class);
			masterEntity.setOperatorId("SYSTEM");
			masterEntity.setRecordInUse(RecordInUseType.Y);
			studentPreferencesService.create(masterEntity);
		}
	}

	public StudentPreferencesWorkDTO getPreferenceFromWork(Long studentId) {
		StudentPreferencesWork studentPreferencesWork = studentPreferencesWorkService.get(studentId);
		if (studentPreferencesWork != null) {
			StudentPreferencesWorkDTO studentPreferencesWorkDTO = modelMapper.map(studentPreferencesWork,
					StudentPreferencesWorkDTO.class);
			return studentPreferencesWorkDTO;
		} else {
			return null;
		}
	}

	public StudentPreferencesDTO getPreferenceFromMaster(Long studentId) {
		StudentPreferences studentPreferences = studentPreferencesService.get(studentId);
		if (studentPreferences != null) {
			StudentPreferencesDTO studentPreferencesDTO = modelMapper.map(studentPreferences,
					StudentPreferencesDTO.class);
			return studentPreferencesDTO;
		} else {
			return null;
		}
	}
}
