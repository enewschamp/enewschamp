package com.enewschamp.app.workinghours.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.workinghours.entity.WorkingHours;

public interface WorkingHourRepository extends JpaRepository<WorkingHours, Long>{
	
	@Query("Select w from WorkingHours w where w.editionId= :editionId")
	public Optional<WorkingHours> getWorkingHours(@Param("editionId") String editionId);
}
