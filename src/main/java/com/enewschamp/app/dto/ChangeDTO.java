package com.enewschamp.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ChangeDTO extends AbstractDTO{	
	

	private static final long serialVersionUID = 2712322957426193892L;

	protected String fieldName;

}
