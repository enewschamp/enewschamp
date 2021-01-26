package com.enewschamp.app.common.uicontrols.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
@JaversSpringDataAuditable
public interface UIControlsRulesRepository extends JpaRepository<UIControlsRules, Long> {

	@Query("select n from UIControlsRules n where n.uiControlId= :uiControlId and n.recordInUse ='Y' order by n.execSeq")
	public List<UIControlsRules> getControlRules(@Param("uiControlId") Long uiControlId);

}
