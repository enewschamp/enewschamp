package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.common.uicontrols.entity.UIControls;

@JaversSpringDataAuditable
public interface UIControlsRepository extends JpaRepository<UIControls, Long>{

	public List<UIControls> findByScreenName(String screenName);
	public UIControls save(UIControls uicontrols);
	public List<UIControls> findByScreenAndAction(String screenName, String action);

	
}
