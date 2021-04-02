package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;

public interface UIControlsRulesRepository extends JpaRepository<UIControlsRules, Long> {

	@Cacheable
	@Query("select n from UIControlsRules n where n.uiControlId= :uiControlId and n.recordInUse ='Y' order by n.execSeq")
	public List<UIControlsRules> getControlRules(@Param("uiControlId") Long uiControlId);

}
