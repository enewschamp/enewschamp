package com.enewschamp.app.common.city.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.city.entity.City;

public interface CityRepository extends JpaRepository<City, Long>{

	@Query("Select c from City c where c.stateId = :stateId and c.countryId= :countryId and c.recordInUse ='Y' ")
	public List<City> getCitiesByState(@Param("stateId")String stateId, @Param("countryId")String countryId);
	
	@Query("Select c from City c where c.countryId= :countryId and c.recordInUse ='Y' ")
	public List<City> getCitiesByCountry(@Param("countryId") String countryId);
}
