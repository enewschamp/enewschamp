package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.common.uicontrols.entity.UIControls;

@JaversSpringDataAuditable
public interface UIControlsRepository extends JpaRepository<UIControls, Long> {

	@Cacheable
	public List<UIControls> findByPageNameAndOperation(String screenName, String operation);

	public UIControls save(UIControls uicontrols);
}
