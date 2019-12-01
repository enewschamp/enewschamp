package com.enewschamp.app.common.uicontrols.entity;

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
@Table(name = "UIControlsRules")
public class UIControlsRules extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UIControlsRules_id_generator")
	@SequenceGenerator(name = "controlsRuleId_generator", sequenceName = "UIControlsRules_seq", allocationSize = 1)
	@Column(name = "ruleId", length = 10)
	private Long ruleId = 0L;

	@Column(name = "uiControlId", length = 10)
	private Long uiControlId = 0L;

	@Column(name = "execSeq")
	private Long execSeq;

	@Column(name = "rule", length = 500)
	private String rule;

	@Column(name = "pluginClass", length = 200)
	private String pluginClass;

	@Column(name = "visibility", length = 100)
	private String visibility;

}
