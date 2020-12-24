package com.enewschamp.publication.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.domain.entity.Avatar;

public interface AvatarRepositoryCustom {
	public Page<Avatar> findAvatars(Pageable pageable);
}
