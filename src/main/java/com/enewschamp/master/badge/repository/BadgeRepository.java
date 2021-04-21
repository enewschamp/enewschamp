package com.enewschamp.master.badge.repository;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.publication.domain.common.BadgeList;
import com.enewschamp.publication.domain.entity.Badge;

@JaversSpringDataAuditable
public interface BadgeRepository extends JpaRepository<Badge, Long> {

	@Query("select d from Badge d where d.editionId= :editionId and d.genreId= :genreId and d.recordInUse ='Y'")
	public Optional<Badge> getBadgeForGenreAndEdition(@Param("editionId") String editionId,
			@Param("genreId") String genreId);

	@Query("select d from Badge d where d.editionId= :editionId and d.recordInUse ='Y'")
	public Optional<Badge> getBadgeForEdition(@Param("editionId") String editionId);

	@Query("select d from Badge d where d.nameId=:nameId and d.genreId=:genreId and d.readingLevel=:readingLevel and d.editionId= :editionId and d.recordInUse ='Y'")
	public Optional<Badge> getBadgeDetails(@Param("nameId") String nameId, @Param("genreId") String genreId,
			@Param("readingLevel") int readingLevel, @Param("editionId") String editionId);

	@Query("select d from Badge d where d.editionId= :editionId and d.readingLevel= :readingLevel and d.genreId= :genreId and d.recordInUse ='Y' and d.monthlyPointsToScore <= :monthlyPointsToScore order by d.monthlyPointsToScore desc")
	public List<Badge> getBadgeForStudent(@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("genreId") String genreId, @Param("monthlyPointsToScore") Long studentpoints);

	@Query("select d from Badge d where d.editionId= :editionId and d.recordInUse ='Y' and d.monthlyPointsToScore > :studentpoints order by d.monthlyPointsToScore")
	public List<Badge> getNextBadge(@Param("editionId") String editionId, @Param("studentpoints") Long studentpoints);

	@Query("select d from Badge d where d.editionId= :editionId and d.recordInUse ='Y' and d.genreId= :genreId and d.readingLevel= :readingLevel and d.monthlyPointsToScore > :studentpoints order by d.monthlyPointsToScore")
	public List<Badge> getNextBadgeForGenre(@Param("editionId") String editionId, @Param("genreId") String genreId,
			@Param("readingLevel") int readingLevel, @Param("studentpoints") Long studentpoints);

	@Query(value = "select a.badgeId as badgeId,a.genreId as genre,a.nameId as name,a.readingLevel as readingLevel,a.monthlyPointsToScore as points,a.imageName as image,a.successImageName as successImage,a.audioFileName as audio from Badge a")
	public List<BadgeList> getBadgeList();
}