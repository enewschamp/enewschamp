package com.enewschamp.app.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Page implements Serializable {
	
	@NotNull
	private HeaderDTO header;
} 
