package com.enewschamp.app.admin.promotion.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
