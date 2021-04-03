package com.enewschamp.app.admin.institutionstakeholder.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.institutionstakeholder.entity.InstitutionStakeholder;

@JaversSpringDataAuditable
public interface InstitutionStakeholderRepository extends JpaRepository<InstitutionStakeholder, Long> {

}
