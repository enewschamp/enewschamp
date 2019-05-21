package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.uicontrols.entity.UIControls;

@JaversSpringDataAuditable
public interface UIControlsRepository extends JpaRepository<UIControls, Long>{

	@Query("select u from UIControls u where u.screenName = :screenName")
	public List<UIControls> findByScreenName(@Param("screenName") String screenName);
	
	public UIControls save(UIControls uicontrols);
	

	
}
