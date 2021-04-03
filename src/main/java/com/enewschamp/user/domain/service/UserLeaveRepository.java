package com.enewschamp.user.domain.service;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.user.domain.entity.UserLeave;
import com.enewschamp.user.domain.entity.UserLeaveKey;

@JaversSpringDataAuditable
interface UserLeaveRepository extends JpaRepository<UserLeave, UserLeaveKey> {

}