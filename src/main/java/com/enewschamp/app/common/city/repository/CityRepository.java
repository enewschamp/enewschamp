package com.enewschamp.app.common.city.repository;

import java.util.List;
import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.publication.domain.common.LOVProjection;

@JaversSpringDataAuditable
public interface CityRepository extends JpaRepository<City, Long> {

	@Query("Select c from City c where c.stateId = :stateId and c.countryId= :countryId and c.recordInUse ='Y' ")
	public List<City> getCitiesByState(@Param("stateId") String stateId, @Param("countryId") String countryId);

	@Query("Select c from City c where c.countryId= :countryId and c.recordInUse ='Y' ")
	public List<City> getCitiesByCountry(@Param("countryId") String countryId);

	@Query("Select c from City c where c.stateId = :stateId and c.countryId= :countryId and c.recordInUse ='Y' and c.isApplicableForNewsEvents='Y' ")
	public List<City> getCitiesByStateForNewsEvent(@Param("stateId") String stateId,
			@Param("countryId") String countryId);

	@Query("Select c from City c where c.countryId= :countryId and c.recordInUse ='Y' and c.isApplicableForNewsEvents='Y' ")
	public List<City> getCitiesByCountryForNewsEvent(@Param("countryId") String countryId);

	@Query(value = "select a.nameId as id, a.description as name from City a where a.recordInUse='Y' and a.isApplicableForNewsEvents='Y'")
	public List<LOVProjection> getCityLOVForNewsEvent();

	@Query("Select c from City c where c.nameId= :nameId and c.recordInUse ='Y' ")
	public Optional<City> getCity(@Param("nameId") String nameId);

	public Optional<City> findByNameIdAndStateIdAndCountryId(String nameId, String stateId, String countryId);

}
