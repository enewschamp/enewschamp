package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;

public interface UIControlsRulesRepository extends JpaRepository<UIControlsRules, Long> {

	@Query("select n from UIControlsRules n where n.uiControlId= :uiControlId and n.recordInUse ='Y' order by n.execSeq")
	public List<UIControlsRules> getControlRules(@Param("uiControlId") Long uiControlId);

	@Modifying
	@Transactional
	@Query(value = "truncate table uicontrols_rules", nativeQuery = true)
	public void truncate();
	
	@Modifying
	@Query(value = "truncate table uicontrols_rules_id_seq", nativeQuery = true)
	public void deleteSequences();
	
	@Modifying
	@Query(value = "insert into uicontrols_rules_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();
}
