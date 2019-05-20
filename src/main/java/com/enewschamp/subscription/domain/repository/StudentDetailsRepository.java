package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domain.entity.StudentDetails;

public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long>{

}
