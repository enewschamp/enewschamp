package com.enewschamp.app.student.badges.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.recognition.page.data.RecognitionData;

public interface StudentBadgesCustomRepository extends JpaRepository<RecognitionData, Long> {

	@Query("select new RecognitionData(d.studentBadgesId as studentBadgesId,b.editionId as editionId,b.readingLevel as readingLevel,d.badgeId as badgeId,b.nameId as badgeName,d.operationDateTime as badgeGrantDate,b.genreId as badgeGenre,d.monthYear as monthYear) from StudentBadges d,Badge b where d.badgeId=b.badgeId and STR_TO_DATE(CONCAT(d.monthYear,'01'),'%Y%m%d')>=:limitDate and d.studentId = :studentId and b.editionId= :editionId and d.recordInUse ='Y' order by d.operationDateTime desc")
	public Page<RecognitionData> getStudentBadges(@Param("studentId") Long studentId,
			@Param("editionId") String editionId, @Param("limitDate") LocalDate limitDate, Pageable pageable);

	@Query("select new RecognitionData(d.studentBadgesId as studentBadgesId,b.editionId as editionId,b.readingLevel as readingLevel,d.badgeId as badgeId,b.nameId as badgeName,d.operationDateTime as badgeGrantDate,b.genreId as badgeGenre,d.monthYear as monthYear)  from StudentBadges d,Badge b where d.badgeId=b.badgeId and d.studentId = :studentId and b.editionId= :editionId and b.readingLevel= :readingLevel and d.recordInUse ='Y' order by d.operationDateTime desc")
	public List<RecognitionData> getLatestBadges(@Param("studentId") Long studentId,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel);

	@Query("select new RecognitionData(d.studentBadgesId as studentBadgesId,b.editionId as editionId,b.readingLevel as readingLevel,d.badgeId as badgeId,b.nameId as badgeName,d.operationDateTime as badgeGrantDate,b.genreId as badgeGenre,d.monthYear as monthYear)  from StudentBadges d,Badge b where d.badgeId=b.badgeId and d.studentId = :studentId and d.monthYear= :monthYear and d.recordInUse ='Y' order by d.operationDateTime desc")
	public List<RecognitionData> getStudentBadgeDetails(@Param("studentId") Long studentId,
			@Param("monthYear") Long monthYear);

}
