package com.enewschamp.app.fw.page.navigation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "PageNavigatorRules", uniqueConstraints = { @UniqueConstraint(columnNames = { "navId", "execSeq" }) })
public class PageNavigatorRules extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagenavigator_rules_id_generator")
	@SequenceGenerator(name = "pagenavigator_rules_id_generator", sequenceName = "pagenavigator_rules_id_seq", allocationSize = 1)
	@Column(name = "ruleId", length = 10)
	private Long ruleId = 0L;

	@Column(name = "navId", length = 10)
	private Long navId = 0L;

	@Column(name = "execSeq")
	private Long execSeq;

	@Column(name = "rule", length = 500)
	private String rule;

	@Column(name = "nextPage", length = 100)
	private String nextPage;

	@Column(name = "nextPageOperation", length = 100)
	private String nextPageOperation;

	@Column(name = "nextPageLoadMethod", length = 100)
	private String nextPageLoadMethod;

	@Column(name = "controlWorkEntryOrExit", length = 10)
	private String controlWorkEntryOrExit;

}
