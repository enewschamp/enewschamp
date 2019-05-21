package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentControl;

public interface StudentControlRepository extends JpaRepository<StudentControl, Long>{

	@Query("Select * from StudentControl where eMail = :eMail")
	public StudentControl searchByEmail(@Param("eMail")String eMail);
}
