package com.enewschamp.app.student.business;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.service.StudentActivityService;
import com.enewschamp.domain.common.RecordInUseType;
import com.google.common.reflect.TypeToken;

@Service
public class StudentActivityBusiness {

	@Autowired
	StudentActivityService studentActivityService;

	@Autowired
	ModelMapper modelMapper;

	public StudentActivityDTO saveActivity(StudentActivityDTO studentActivityDTO) {
		StudentActivity studentActivity = modelMapper.map(studentActivityDTO, StudentActivity.class);
		
		// TO DO to be corrected
		studentActivity.setRecordInUse(RecordInUseType.Y);
		studentActivity.setOperatorId("SYSTEM");
		
		studentActivity = studentActivityService.create(studentActivity);
		StudentActivityDTO studentActivityNew = modelMapper.map(studentActivity, StudentActivityDTO.class);

		return studentActivityNew;
	}

	public StudentActivityDTO getActivity(Long studentId, Long newsArticleId) {
		StudentActivity studentActivity = null;

		try {
			 studentActivity = studentActivityService.getStudentActivity(studentId, newsArticleId);
		}catch(Exception e)
		{
			return null;
		}
		StudentActivityDTO studentActivityDTO = modelMapper.map(studentActivity, StudentActivityDTO.class);
		return studentActivityDTO;
	}

	public List<StudentActivityDTO> getSavedArticles(Long studentId) {
		List<StudentActivity> studentActivity = null;

		try {
			 studentActivity = studentActivityService.getsavedArticles(studentId);
		}catch(Exception e)
		{
			return null;
		}
		java.lang.reflect.Type listType = new TypeToken<List<StudentActivityDTO>>(){}.getType();

		List<StudentActivityDTO> studentActivityList = modelMapper.map(studentActivity,listType);
		
		return studentActivityList;
	}

	
	public StudentActivityDTO saveQuizData(Long studentId, Long newsArticleId, Long quizQCorrect) {
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);

		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setQuizScore(quizQCorrect);
			studentActivityDTO = saveActivity(studentActivityDTONew);

		} else {
			Long quizScore = (studentActivityDTO.getQuizScore()==null) ? 0 : studentActivityDTO.getQuizScore();
			quizScore = +quizQCorrect;
			studentActivityDTO.setQuizScore(quizScore);
			studentActivityDTO = saveActivity(studentActivityDTO);

		}
		return studentActivityDTO;
	}
	
	public StudentActivityDTO saveOpinion(Long studentId, Long newsArticleId, String opinion)
	{
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setOpinion(opinion);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		}
		else
		{
			studentActivityDTO.setOpinion(opinion);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		return studentActivityDTO;
	}
	
	public StudentActivityDTO likeArticle(Long studentId, Long newsArticleId, String likeFlag)
	{
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setLikeLevel(likeFlag);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		}
		else
		{
			studentActivityDTO.setLikeLevel(likeFlag);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		return studentActivityDTO;
	}
	
	
	public StudentActivityDTO saveArticle(Long studentId, Long newsArticleId, String saveFlag)
	{
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setSaved(saveFlag);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		}
		else
		{
			studentActivityDTO.setSaved(saveFlag);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		return studentActivityDTO;
	}
}
