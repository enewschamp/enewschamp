package com.enewschamp.master.badge.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.master.badge.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, String>{

	@Query("select d from Badge d where d.editionId= :editionId and d.genreId= :genreId and d.recordInUse ='Y'")
	public Optional<Badge> getBadgeForGenreAndEdition( @Param("editionId") String editionId, @Param("genreId")  String genreId);
	
	@Query("select d from Badge d where d.editionId= :editionId and d.recordInUse ='Y'")
	public Optional<Badge> getBadgeForEdition( @Param("editionId") String editionId);
	
	@Query("select d from Badge d where d.editionId= :editionId and d.genreId= :genreId and d.recordInUse ='Y' and d.monthlyPointsToScore <= :monthlyPointsToScore ")
	public Optional<Badge> getBadgeForStudent( @Param("editionId") String editionId, @Param("genreId")  String genreId, @Param("monthlyPointsToScore") Long studentpoints);
	
}
