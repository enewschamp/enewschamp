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
@EqualsAndHashCode(callSuper = false)
@Table(name = "PageNavigator")
public class PageNavigator extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_navigator_id_generator")
	@SequenceGenerator(name = "page_navigator_id_generator", sequenceName = "page_navigator_id_seq", allocationSize = 1)
	@Column(name = "navId", length = 10)
	private Long navId = 0L;

	@Column(name = "currentPage")
	private String currentPage;

	@Column(name = "operation")
	private String operation;

	@Column(name = "action")
	private String action;

	@Column(name = "nextPage")
	private String nextPage;

	@Column(name = "submissionMethod")
	private String submissionMethod;

	@Column(name = "workToMaster", length = 200)
	private String workToMaster;

	@Column(name = "nextPageOperation")
	private String nextPageOperation;

	@Column(name = "nextPageLoadMethod")
	private String nextPageLoadMethod;

	@Column(name = "controlWorkEntryOrExit", length = 10)
	private String controlWorkEntryOrExit;

	@Column(name = "updationTable", length = 1)
	private String updationTable;
}
