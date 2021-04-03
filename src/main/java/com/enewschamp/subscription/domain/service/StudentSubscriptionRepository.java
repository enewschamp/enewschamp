package com.enewschamp.subscription.domain.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.subscription.domain.entity.StudentSubscription;

public interface StudentSubscriptionRepository extends JpaRepository<StudentSubscription, Long> {

	@Query("Select s from StudentSubscription s where s.autoRenewal ='Y' and s.subscriptionId IS NOT NULL and s.endDate<CURDATE() order by s.endDate asc")
	public List<StudentSubscription> getSubscriptionRenewalList();
}
