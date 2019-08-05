package com.enewschamp.app.common.state.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.state.entity.State;

public interface StateRepository extends JpaRepository<State, Long>{

	@Query("Select s from State s where s.countryId= :countryId and s.recordInUse = 'Y' ")
	public List<State> getStateForCountry(@Param("countryId" )String countryId);
}