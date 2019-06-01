package com.enewschamp.app.common.city.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CityDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long cityId;
	
	private String nameId;

	private String description;
	
	
	private String stateId;
	
	private String countryId;
	

}
