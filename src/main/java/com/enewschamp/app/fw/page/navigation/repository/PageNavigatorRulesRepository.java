package com.enewschamp.app.fw.page.navigation.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;

@JaversSpringDataAuditable
public interface PageNavigatorRulesRepository extends JpaRepository<PageNavigatorRules, Long> {

	@Query("select n from PageNavigatorRules n where n.navId= :navId and n.recordInUse ='Y' order by n.execSeq")
	public List<PageNavigatorRules> getNavRules(@Param("navId") Long navId);

}
