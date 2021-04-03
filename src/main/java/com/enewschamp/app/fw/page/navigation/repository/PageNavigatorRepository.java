package com.enewschamp.app.fw.page.navigation.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.fw.page.navigation.entity.PageNavigator;

public interface PageNavigatorRepository extends JpaRepository<PageNavigator, Long> {

	@Cacheable
	@Query("select n from PageNavigator n where n.operation= :operation and n.navId< (select navId from PageNavigator bb where bb.currentPage= :currPage and bb.operation= :operation) and n.updationTable='W'")
	public List<PageNavigator> getNavList(@Param("operation") String operation, @Param("currPage") String currPage);

	@Cacheable
	@Query("select n from PageNavigator n where n. action=:action and n.operation= :operation and n.currentPage= :currPage")
	public PageNavigator getNavPage(@Param("action") String action, @Param("operation") String operation,
			@Param("currPage") String currPage);
	
	@Modifying
	@Transactional
	@Query(value = "truncate table page_navigator", nativeQuery = true)
	public void truncate();
	
	@Modifying
	@Query(value = "truncate table page_navigator_id_seq", nativeQuery = true)
	public void deleteSequences();
	
	@Modifying
	@Query(value = "insert into page_navigator_id_seq values(1)", nativeQuery = true)
	public void initializeSequence();
}