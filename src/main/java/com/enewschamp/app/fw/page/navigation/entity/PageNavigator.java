package com.enewschamp.app.fw.page.navigation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name="PageNavigator")
public class PageNavigator extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PageNavigator_id_generator")
	@SequenceGenerator(name="navId_generator", sequenceName = "PageNavigator_seq", allocationSize=1)
	@Column(name = "navId", length=10)
	private Long navId= 0L;
	
	@Column(name="action")
	private String action;
	
	@Column(name="currentPage")
	private String currentPage;
	
	@Column(name="nextpage")
	private String nextpage;
	
	@Column(name="previousPage")
	private String previousPage;
	
	@Column(name="operation")	
	private String operation;
	
	@Column(name="commitMasterData")	
	private String commitMasterData;
	
	@Column(name="updationTable", length=1)	
	private String updationTable;
	
	@Column(name="processSeq", length=1)	
	private int processSeq;
}
