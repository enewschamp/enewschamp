package com.enewschamp.app.common.state.repository;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.state.entity.State;

@JaversSpringDataAuditable
public interface StateRepository extends JpaRepository<State, Long> {

	@Query("Select s from State s where s.countryId= :countryId and s.recordInUse = 'Y' ")
	public List<State> getStateForCountry(@Param("countryId") String countryId);

	@Query("Select s from State s where s.nameId= :nameId and s.recordInUse = 'Y' ")
	public Optional<State> getState(@Param("nameId") String nameId);
	public  Optional<State> findByNameIdAndCountryId(String nameId, String countryId);
}
