package com.enewschamp.subscription.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@JaversSpringDataAuditable
public interface StudentSubscriptionWorkRepository extends JpaRepository<StudentSubscriptionWork, Long>{

}
