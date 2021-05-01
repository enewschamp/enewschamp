package com.enewschamp.app.admin.institution.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.admin.institution.entity.InstitutionAddress;

@JaversSpringDataAuditable
public interface InstitutionAddressRepository extends JpaRepository<InstitutionAddress, Long> {

}
