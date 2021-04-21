package com.enewschamp.app.common.uicontrols.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;

@JaversSpringDataAuditable
public interface UIControlsGlobalRepository extends JpaRepository<UIControlsGlobal, Long> {

	@Cacheable
	public UIControlsGlobal findByGlobalControlRef(String globalControlRef);

	public UIControlsGlobal save(UIControlsGlobal uicontrolGlobal);

	@Modifying
	@Transactional
	@Query(value = "truncate table uicontrols_global", nativeQuery = true)
	public void truncate();

	@Modifying
	@Query(value = "truncate table uicontrols_global_id_seq", nativeQuery = true)
	public void deleteSequences();

	@Modifying
	@Query(value = "insert into uicontrols_global_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();
}
