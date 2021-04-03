package com.enewschamp.app.admin.celebration.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.celebration.entity.Celebration;

@JaversSpringDataAuditable
public interface CelebrationRepository extends JpaRepository<Celebration, Long> {

}
