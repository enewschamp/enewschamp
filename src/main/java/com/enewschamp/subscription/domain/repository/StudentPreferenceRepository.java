package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.subscription.domin.entity.StudentPreference;

public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, Long>{

}