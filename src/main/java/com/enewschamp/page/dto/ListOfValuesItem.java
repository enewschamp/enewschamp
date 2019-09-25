package com.enewschamp.page.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ListOfValuesItem implements Serializable {
	
	private static final long serialVersionUID = -7125303529322853135L;

	@NotNull
	private String name;
	
	@NotNull
	private String id;

}
