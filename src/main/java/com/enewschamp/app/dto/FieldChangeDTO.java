package com.enewschamp.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FieldChangeDTO extends ChangeDTO {	
	

	private static final long serialVersionUID = 8768254330371744350L;

	private Object oldValue;
	private Object newValue;
	
	public FieldChangeDTO(String fieldName, Object oldValue, Object newValue) {
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		
	}
}
