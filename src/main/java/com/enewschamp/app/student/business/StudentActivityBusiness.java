package com.enewschamp.app.student.business;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.service.StudentActivityService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;
import com.google.common.reflect.TypeToken;

@Service
public class StudentActivityBusiness {

	@Autowired
	StudentActivityService studentActivityService;

	@Autowired
	private NewsArticleService newsArticleService;

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
		} catch (Exception e) {
			return null;
		}
		StudentActivityDTO studentActivityDTO = modelMapper.map(studentActivity, StudentActivityDTO.class);
		return studentActivityDTO;
	}

	public List<StudentActivityDTO> getSavedArticles(Long studentId) {
		List<StudentActivity> studentActivity = null;
		try {
			studentActivity = studentActivityService.getSavedArticles(studentId);
		} catch (Exception e) {
			return null;
		}
		java.lang.reflect.Type listType = new TypeToken<List<StudentActivityDTO>>() {
		}.getType();

		List<StudentActivityDTO> studentActivityList = modelMapper.map(studentActivity, listType);

		return studentActivityList;
	}

	public StudentActivityDTO saveQuizData(Long studentId, String editionId, int readingLevel, Long newsArticleId,
			Long quizQCorrect) {
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setEditionId(editionId);
			studentActivityDTONew.setReadingLevel(Long.valueOf(readingLevel));
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setQuizScore(quizQCorrect);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		} else {
			Long quizScore = (studentActivityDTO.getQuizScore() == null) ? 0 : studentActivityDTO.getQuizScore();
			quizScore = +quizQCorrect;
			studentActivityDTO.setQuizScore(quizScore);
			studentActivityDTO = saveActivity(studentActivityDTO);

		}
		return studentActivityDTO;
	}

	public StudentActivityDTO saveOpinion(Long studentId, Long newsArticleId, String opinion, int readingLevel,
			String editionId) {
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setOpinion(opinion);
			studentActivityDTONew.setReadingLevel(Long.valueOf(readingLevel));
			studentActivityDTONew.setEditionId(editionId);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		} else {
			studentActivityDTO.setOpinion(opinion);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		return studentActivityDTO;
	}

	public StudentActivityDTO likeArticle(Long studentId, Long newsArticleId, String likeFlag, int readingLevel,
			String editionId) {
		String currentLikeLevel = "";
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setLikeLevel(likeFlag);
			studentActivityDTONew.setReadingLevel(Long.valueOf(readingLevel));
			studentActivityDTONew.setEditionId(editionId);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		} else {
			currentLikeLevel = studentActivityDTO.getLikeLevel();
			studentActivityDTO.setLikeLevel(likeFlag);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		NewsArticle article = newsArticleService.get(newsArticleId);
		if ("O".equalsIgnoreCase(likeFlag)) {
			article.setLikeOCount(article.getLikeOCount() == null ? 1 : (article.getLikeOCount() + 1));
		} else if ("H".equalsIgnoreCase(likeFlag)) {
			article.setLikeHCount(article.getLikeHCount() == null ? 1 : (article.getLikeHCount() + 1));
		} else if ("L".equalsIgnoreCase(likeFlag)) {
			article.setLikeLCount(article.getLikeLCount() == null ? 1 : (article.getLikeLCount() + 1));
		} else if ("S".equalsIgnoreCase(likeFlag)) {
			article.setLikeSCount(article.getLikeSCount() == null ? 1 : (article.getLikeSCount() + 1));
		} else if ("W".equalsIgnoreCase(likeFlag)) {
			article.setLikeWCount(article.getLikeWCount() == null ? 1 : (article.getLikeWCount() + 1));
		}
		if ("O".equalsIgnoreCase(currentLikeLevel)) {
			article.setLikeOCount(article.getLikeOCount() == 0 ? 0 : (article.getLikeOCount() - 1));
		} else if ("H".equalsIgnoreCase(currentLikeLevel)) {
			article.setLikeHCount(article.getLikeHCount() == 0 ? 0 : (article.getLikeHCount() - 1));
		} else if ("L".equalsIgnoreCase(currentLikeLevel)) {
			article.setLikeLCount(article.getLikeLCount() == 0 ? 0 : (article.getLikeLCount() - 1));
		} else if ("S".equalsIgnoreCase(currentLikeLevel)) {
			article.setLikeSCount(article.getLikeSCount() == 0 ? 0 : (article.getLikeSCount() - 1));
		} else if ("W".equalsIgnoreCase(currentLikeLevel)) {
			article.setLikeWCount(article.getLikeWCount() == 0 ? 0 : (article.getLikeWCount() - 1));
		}
		newsArticleService.updateLike(article);
		return studentActivityDTO;
	}

	public StudentActivityDTO saveArticle(Long studentId, Long newsArticleId, String saveFlag, int readingLevel,
			String editionId) {
		StudentActivityDTO studentActivityDTO = getActivity(studentId, newsArticleId);
		if (studentActivityDTO == null) {
			StudentActivityDTO studentActivityDTONew = new StudentActivityDTO();
			studentActivityDTONew.setStudentId(studentId);
			studentActivityDTONew.setNewsArticleId(newsArticleId);
			studentActivityDTONew.setSaved(saveFlag);
			studentActivityDTONew.setReadingLevel(Long.valueOf(readingLevel));
			studentActivityDTONew.setEditionId(editionId);
			studentActivityDTO = saveActivity(studentActivityDTONew);
		} else {
			studentActivityDTO.setSaved(saveFlag);
			studentActivityDTO = saveActivity(studentActivityDTO);
		}
		return studentActivityDTO;
	}
}
