package com.enewschamp.app.admin.student.control.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.subscription.domain.entity.StudentControl;

public interface StudentControlRepositoryCustom {
	public Page<StudentControl> findStudentControls(Pageable pageable);
}

