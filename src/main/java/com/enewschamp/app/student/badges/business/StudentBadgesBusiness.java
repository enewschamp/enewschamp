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
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyByGenreDTO;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
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

	public StudentBadgesDTO getDailyTrend(Long studentId, String editionId, Long monthYear) {
		StudentBadges studentBadges = studentBadgesService.getStudentBadges(studentId, editionId, monthYear);
		StudentBadgesDTO studentActivityDTO = modelMapper.map(studentBadges, StudentBadgesDTO.class);
		return studentActivityDTO;
	}

	public StudentBadges grantGenreBadge(Long studentId, String editionId, int readingLevel, Long monthYear,
			String genreId, TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO) {
		Badge badge = bagdeService.getBadgeForStudent(editionId, readingLevel, genreId,
				trendsMonthlyByGenreDTO.getQuizCorrect());
		StudentBadges studBadge = null;
		if (badge != null) {
			studBadge = new StudentBadges();
			studBadge.setStudentId(studentId);
			studBadge.setBadgeId(badge.getBadgeId());
			studBadge.setMonthYear(monthYear);
			studBadge.setOperatorId("SYSTEM");
			studBadge.setRecordInUse(RecordInUseType.Y);
			studentBadgesService.create(studBadge);
		}
		return studBadge;
	}

	public StudentBadges grantBadge(Long studentId, String editionId, int readingLevel, Long monthYear, String genreId,
			TrendsMonthlyTotalDTO trendsMonthlyTotalDTO) {
		Badge badge = bagdeService.getBadgeForStudent(editionId, readingLevel, genreId,
				trendsMonthlyTotalDTO.getQuizCorrect());
		StudentBadges studBadge = null;
		if (badge != null) {
			studBadge = new StudentBadges();
			studBadge.setStudentId(studentId);
			studBadge.setBadgeId(badge.getBadgeId());
			studBadge.setMonthYear(monthYear);
			studBadge.setOperatorId("SYSTEM");
			studBadge.setRecordInUse(RecordInUseType.Y);
			studentBadgesService.create(studBadge);
		}
		return studBadge;
	}

	public Page<RecognitionData> getStudentBadges(Long studentId, String editionId, LocalDate limitDate, int pageNo,
			int pageSize) {
		Page<RecognitionData> studentBadges = studentBadgesService.getStudentbadges(studentId, editionId, limitDate,
				pageNo, pageSize);
		return studentBadges;
	}

	public RecognitionData getLastestBadge(Long studentId, String editionId, int readingLevel) {
		RecognitionData latestBadge = studentBadgesService.getLastestbadge(studentId, editionId, readingLevel);
		return latestBadge;
	}

	public List<RecognitionData> getBadgeDetails(Long studentId, String editionId, int readingLevel, Long monthYear) {
		List<RecognitionData> latestBadge = studentBadgesService.getStudentBadgeDetails(studentId, editionId,
				readingLevel, monthYear);
		return latestBadge;
	}

}
