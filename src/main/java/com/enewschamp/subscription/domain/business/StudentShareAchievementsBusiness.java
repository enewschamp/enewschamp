package com.enewschamp.subscription.domain.business;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentShareAchievementsDTO;
import com.enewschamp.subscription.domain.entity.StudentShareAchievements;
import com.enewschamp.subscription.domain.service.StudentShareAchievementsService;
import com.google.common.reflect.TypeToken;

@Service
public class StudentShareAchievementsBusiness {

	@Autowired
	StudentShareAchievementsService studentShareAchievementsService;

	@Autowired
	ModelMapper modelMapper;

	public StudentShareAchievements saveAsMaster(StudentShareAchievementsDTO studentShareAchievementsDTO) {

		StudentShareAchievements studentAchievements = modelMapper.map(studentShareAchievementsDTO,
				StudentShareAchievements.class);
		studentAchievements = studentShareAchievementsService.create(studentAchievements);
		return studentAchievements;
	}

	public List<StudentShareAchievementsDTO> getStudentDetailsFromMaster(Long studentId) {
		List<StudentShareAchievements> studentShareAchievements = studentShareAchievementsService
				.getStudentAchievements(studentId);
		if (studentShareAchievements.isEmpty()) {
			return null;
		}
		java.lang.reflect.Type listType = new TypeToken<List<StudentShareAchievementsDTO>>() {
		}.getType();
		List<StudentShareAchievementsDTO> studentShareAchievementsDTO = modelMapper.map(studentShareAchievements,
				listType);
		return studentShareAchievementsDTO;
	}

}
