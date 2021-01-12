package com.enewschamp.app.student.badges.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.badges.repository.StudentBadgesCustomRepository;
import com.enewschamp.app.student.badges.repository.StudentBadgesRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentBadgesService {

	@Autowired
	StudentBadgesRepository studentBadgesRepository;

	@Autowired
	StudentBadgesCustomRepository studentBadgesCustomRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public StudentBadges create(StudentBadges studentBadgesEntity) {
		StudentBadges studentBadges= null;
		try {
			studentBadges = studentBadgesRepository.save(studentBadgesEntity);
		}
		catch(DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentBadges;
	}

	public StudentBadges update(StudentBadges studentBadgesEntity) {
		Long scoresDailyId = studentBadgesEntity.getStudentBadgesId();
		StudentBadges existingStudentBadges = get(scoresDailyId);
		if(existingStudentBadges.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(studentBadgesEntity, existingStudentBadges);
		return studentBadgesRepository.save(existingStudentBadges);
	}

	public StudentBadges patch(StudentBadges scoresDaily) {
		Long navId = scoresDaily.getStudentBadgesId();
		StudentBadges existingEntity = get(navId);
		modelMapperForPatch.map(scoresDaily, existingEntity);
		return studentBadgesRepository.save(existingEntity);
	}

	public void delete(Long StudentBadgesId) {
		studentBadgesRepository.deleteById(StudentBadgesId);
	}

	public StudentBadges get(Long scoresDailyId) {
		Optional<StudentBadges> existingEntity = studentBadgesRepository.findById(scoresDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUD_BADGES_NOT_FOUND);

		}
	}

	public StudentBadges getStudentBadges(Long studentId, String editionId, Long yearMonth) {
		Optional<StudentBadges> existingEntity = studentBadgesRepository.getStudentBadges(studentId, yearMonth);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUD_BADGES_NOT_FOUND);
		}
	}

	public List<RecognitionData> getStudentBadgeDetails(Long studentId, String editionId, int readingLevel,
			Long yearMonth) {
		List<RecognitionData> studentBadges = studentBadgesCustomRepository.getStudentBadgeDetails(studentId,
				yearMonth);
		return studentBadges;
	}

	public Page<RecognitionData> getStudentbadges(Long studentId, String editionId, LocalDate limitDate, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<RecognitionData> studentPage = studentBadgesCustomRepository.getStudentBadges(studentId, editionId,
				limitDate, pageable);
		return studentPage;
	}

	public RecognitionData getLastestbadge(Long studentId, String editionId, int readingLevel) {
		List<RecognitionData> studentBadgeList = studentBadgesCustomRepository.getLatestBadges(studentId, editionId,
				readingLevel);
		RecognitionData badge = null;
		if (!studentBadgeList.isEmpty()) {
			badge = studentBadgeList.get(0);
		}
		return badge;
	}

	public Long isBadgeUnlocked(Long badgeId, Long studentId, Long yearMonth) {
		Long unlockBadgeId = studentBadgesCustomRepository.isBadgeUnlocked(badgeId, studentId, yearMonth);
		return unlockBadgeId;
	}

	public RecognitionData getBadgeDetails(Long studentId, String editionId, int readingLevel) {
		List<RecognitionData> studentBadgeList = studentBadgesCustomRepository.getLatestBadges(studentId, editionId,
				readingLevel);
		RecognitionData badge = null;
		if (!studentBadgeList.isEmpty()) {
			badge = studentBadgeList.get(0);
		}
		return badge;
	}
}
