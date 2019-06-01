package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentControl;

public interface StudentControlRepository extends JpaRepository<StudentControl, Long>{

	@Query("Select s from StudentControl s where s.emailID = :emailID")
	public StudentControl searchByEmail(@Param("emailID")String eMail);
}
