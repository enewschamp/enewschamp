package com.enewschamp.subscription.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPaymentFailed;

public interface StudentPaymentFailedRepository extends JpaRepository<StudentPaymentFailed, Long> {

	@Query("Select s from StudentPaymentFailed s where s.orderId = :orderId")
	public Optional<StudentPaymentFailed> getByOrderId(@Param("orderId") String orderId);

}
