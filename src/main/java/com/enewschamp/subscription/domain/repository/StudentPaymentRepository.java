package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domin.entity.StudentPayment;

public interface StudentPaymentRepository extends JpaRepository<StudentPayment, Long>{

}
