package com.enewschamp.app.admin.promotion.repository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@JaversSpringDataAuditable
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	@Query("Select s from Promotion s where s.editionId= :editionId and s.dateFrom>=SYSDATE() and s.dateTo<=SYSDATE()")
	public List<Promotion> getActivePromotionList(@Param("editionId") String editionId);

}
