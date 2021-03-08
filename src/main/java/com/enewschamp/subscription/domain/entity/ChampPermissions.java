package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class ChampPermissions {
	/**
	 */

	private static final long serialVersionUID = 1L;

	@Column(name = "champCity", length = 1)
	private String champCity;

	@Column(name = "champProfilePic", length = 1)
	private String champProfilePic;

	@Column(name = "champSchool", length = 1)
	private String champSchool;
}
