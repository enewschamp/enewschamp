package com.enewschamp.app.common.country.repository;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.admin.dashboard.handler.CountryView;
import com.enewschamp.app.common.country.entity.Country;
@JaversSpringDataAuditable
public interface CountryRepository extends JpaRepository<Country, Long> {

	@Query("Select c from Country c")
	public List<Country> getAll();

	@Query("Select c from Country c where c.recordInUse='Y'")
	public List<Country> getCountries();

	@Query("Select c from Country c where c.recordInUse='Y' and  c.nameId= :nameId")
	public Optional<Country> getCountry(@Param("nameId") String nameId);
	
	public List<CountryView> findAllProjectedBy();
}
