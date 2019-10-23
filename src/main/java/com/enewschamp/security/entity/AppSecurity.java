package com.enewschamp.app.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity
public class AppSecurity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_sec_id_generator")
	@SequenceGenerator(name="app_sec_Id_generator", sequenceName = "app_Sec_seq", allocationSize=1)
	@Column(name = "appSecId", updatable = false, nullable = false)
	private Long appSecId;
	

	
	@Column(name="appName", length=20)
	private String appName;
	
	@Column(name="appKey", length=256)
	private String appKey;
	
	
}
