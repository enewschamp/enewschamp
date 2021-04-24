package com.enewschamp.app.fw.page.navigation.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;

@JaversSpringDataAuditable
public interface PageNavigatorRulesRepository extends JpaRepository<PageNavigatorRules, Long> {

	@Cacheable
	@Query("select n from PageNavigatorRules n where n.navId= :navId and n.recordInUse ='Y' order by n.execSeq")
	public List<PageNavigatorRules> getNavRules(@Param("navId") Long navId);

	@Modifying
	@Transactional
	@Query(value = "truncate table page_navigator_rules", nativeQuery = true)
	public void truncate();

	@Modifying
	@Query(value = "truncate table pagenavigator_rules_id_seq", nativeQuery = true)
	public void deleteSequences();

	@Modifying
	@Query(value = "insert into pagenavigator_rules_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();

}
