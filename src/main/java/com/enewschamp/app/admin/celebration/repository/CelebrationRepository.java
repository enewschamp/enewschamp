package com.enewschamp.app.admin.celebration.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.admin.celebration.entity.Celebration;

@JaversSpringDataAuditable
public interface CelebrationRepository extends JpaRepository<Celebration, Long> {

	@Query("Select s from Celebration s where s.editionId= :editionId and s.date=SYSDATE()")
	public List<Celebration> getCelebrationListForToday(@Param("editionId") String editionId);

}
