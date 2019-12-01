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
@Table(name = "PageNavigatorRules")
public class PageNavigatorRules extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PageNavigatorRules_id_generator")
	@SequenceGenerator(name = "navRuleId_generator", sequenceName = "PageNavigatorRules_seq", allocationSize = 1)
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

	@Column(name = "pluginClass", length = 200)
	private String pluginClass;

}
