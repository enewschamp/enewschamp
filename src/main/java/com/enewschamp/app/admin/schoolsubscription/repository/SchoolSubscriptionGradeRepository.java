package com.enewschamp.app.admin.schoolsubscription.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.schoolsubscription.entity.SchoolSubscriptionGrade;

@JaversSpringDataAuditable
public interface SchoolSubscriptionGradeRepository extends JpaRepository<SchoolSubscriptionGrade, Long> {
}
