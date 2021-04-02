package com.enewschamp.app.common.uicontrols.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;

@JaversSpringDataAuditable
public interface UIControlsGlobalRepository extends JpaRepository<UIControlsGlobal, Long> {

	@Cacheable
	public UIControlsGlobal findByGlobalControlRef(String globalControlRef);

	public UIControlsGlobal save(UIControlsGlobal uicontrolGlobal);
}
