package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.common.uicontrols.entity.UIControls;

@JaversSpringDataAuditable
public interface UIControlsRepository extends JpaRepository<UIControls, Long> {

	@Cacheable
	public List<UIControls> findByPageNameAndOperation(String screenName, String operation);

	public UIControls save(UIControls uicontrols);

	@Modifying
	@Transactional
	@Query(value = "truncate table uicontrols", nativeQuery = true)
	public void truncate();

	@Modifying
	@Query(value = "truncate table uicontrols_id_seq", nativeQuery = true)
	public void deleteSequences();

	@Modifying
	@Query(value = "insert into uicontrols_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();
}
