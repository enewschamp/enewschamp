package com.enewschamp.app.student.badges.business;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.student.badges.dto.StudentBadgesDTO;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.badges.service.StudentBadgesService;
import com.enewschamp.app.student.scores.dto.ScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.ScoresMonthlyTotalDTO;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.publication.domain.entity.Badge;

@Service
public class StudentBadgesBusiness {

	@Autowired
	StudentBadgesService studentBadgesService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	BadgeService bagdeService;

	public StudentBadgesDTO saveAcvitity(StudentBadgesDTO studentBadgesDTO) {
		StudentBadges studentBadges = modelMapper.map(studentBadgesDTO, StudentBadges.class);
		studentBadges = studentBadgesService.create(studentBadges);
		StudentBadgesDTO studentBadgesDTONew = modelMapper.map(studentBadges, StudentBadgesDTO.class);
		return studentBadgesDTONew;
	}

	public StudentBadgesDTO getScoresDaily(Long studentId, String editionId, Long yearMonth) {
		StudentBadges studentBadges = studentBadgesService.getStudentBadges(studentId, editionId, yearMonth);
		StudentBadgesDTO studentActivityDTO = modelMapper.map(studentBadges, StudentBadgesDTO.class);
		return studentActivityDTO;
	}

	public StudentBadges grantGenreBadge(Long studentId, String emailId, String editionId, int readingLevel,
			Long yearMonth, String genreId, ScoresMonthlyGenreDTO scoresMonthlyGenreDTO) {
		Badge badge = bagdeService.getBadgeForStudent(editionId, readingLevel, genreId,
				scoresMonthlyGenreDTO.getQuizCorrect());
		StudentBadges studBadge = null;
		if (badge != null) {
			Long unlockBadgeId = studentBadgesService.isBadgeUnlocked(badge.getBadgeId(), studentId, yearMonth);
			if (unlockBadgeId == null || unlockBadgeId == 0) {
				studBadge = new StudentBadges();
				studBadge.setStudentId(studentId);
				studBadge.setBadgeId(badge.getBadgeId());
				studBadge.setYearMonth(yearMonth);
				studBadge.setOperatorId("" + studentId);
				studBadge.setRecordInUse(RecordInUseType.Y);
				studentBadgesService.create(studBadge);
			}
		}
		return studBadge;
	}

	public StudentBadges grantBadge(Long studentId, String emailId, String editionId, int readingLevel, Long yearMonth,
			String genreId, ScoresMonthlyTotalDTO scoresMonthlyTotalDTO) {
		Badge badge = bagdeService.getBadgeForStudent(editionId, readingLevel, genreId,
				scoresMonthlyTotalDTO.getQuizCorrect());
		StudentBadges studBadge = null;
		if (badge != null) {
			Long unlockBadgeId = studentBadgesService.isBadgeUnlocked(badge.getBadgeId(), studentId, yearMonth);
			if (unlockBadgeId == null || unlockBadgeId == 0) {
				studBadge = new StudentBadges();
				studBadge.setStudentId(studentId);
				studBadge.setBadgeId(badge.getBadgeId());
				studBadge.setYearMonth(yearMonth);
				studBadge.setOperatorId("" + studentId);
				studBadge.setRecordInUse(RecordInUseType.Y);
				studentBadgesService.create(studBadge);
			}
		}
		return studBadge;
	}

	public Page<RecognitionData> getStudentBadges(Long studentId, String editionId, LocalDate limitDate, int pageNo,
			int pageSize) {
		Page<RecognitionData> studentBadges = studentBadgesService.getStudentBadges(studentId, editionId, limitDate,
				pageNo, pageSize);
		return studentBadges;
	}

	public RecognitionData getLatestBadge(Long studentId, String editionId, int readingLevel) {
		RecognitionData latestBadge = studentBadgesService.getLatestBadge(studentId, editionId, readingLevel);
		return latestBadge;
	}

	public List<RecognitionData> getBadgeDetails(Long studentId, String editionId, int readingLevel, Long yearMonth) {
		List<RecognitionData> latestBadge = studentBadgesService.getStudentBadgeDetails(studentId, editionId,
				readingLevel, yearMonth);
		return latestBadge;
	}

}
