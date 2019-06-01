package com.enewschamp.subscription.domain.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domain.entity.StudentSchool;

@JaversSpringDataAuditable
public interface StudentSchoolRepository extends JpaRepository<StudentSchool, Long>{

}
