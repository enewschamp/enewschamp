package com.enewschamp.subscription.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domain.entity.StudentSubscription;


public interface StudentSubscriptionRepository extends JpaRepository<StudentSubscription, Long>{

}
