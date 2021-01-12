package com.enewschamp.app.admin.user.role.handler;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.enewschamp.user.app.dto.UserRoleKeyDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserRolePageData extends PageData {
	private static final long serialVersionUID = 1L;

	private UserRoleKeyDTO userRoleKey;

	@NotNull
	@Column(name = "Contribution")
	private int contribution = 0;
	
	@Column(name = "Comments", length = 300)
	private String comments;

	@Column(name = "Contributions", length = 300)
	private String contributions;

}
