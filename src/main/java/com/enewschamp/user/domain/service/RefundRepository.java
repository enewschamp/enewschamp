package com.enewschamp.user.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.user.domain.entity.StudentRefund;

@JaversSpringDataAuditable
interface RefundRepository extends JpaRepository<StudentRefund, Long> {
}
