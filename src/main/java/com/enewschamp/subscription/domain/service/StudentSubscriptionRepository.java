package com.enewschamp.subscription.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentSubscription;

public interface StudentSubscriptionRepository extends JpaRepository<StudentSubscription, Long> {

	@Query("Select s from StudentSubscription s where s.subscriptionSelected<>'F' and s.autoRenewal ='Y' and s.subscriptionId IS NOT NULL and s.endDate<= :endDate order by s.endDate asc")
	public List<StudentSubscription> getSubscriptionRenewalList(@Param("endDate") LocalDate endDate);
}
