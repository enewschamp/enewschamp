package com.enewschamp.app.common.uicontrols.entity;

import java.util.Date;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UIControls")
public class UIControls extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uicontrols_id_generator")
	@SequenceGenerator(name = "uicontrols_id_generator", sequenceName = "uicontrols_seq", allocationSize = 1)
	@Column(name = "uiControlId", updatable = false, nullable = false)
	private Long uiControlId;

	@NotNull
	@Column(name = "ControlName", length = 50)
	private String controlName;

	@NotNull
	@Column(name = "PageName", length = 50)
	private String pageName;

	@NotNull
	@Column(name = "Operation", length = 50)
	private String operation;

	@NotNull
	@Column(name = "controlType", length = 50)
	private String controlType;

	@NotNull
	@Column(name = "DataSize", length = 4)
	private String dataSize;

	@NotNull
	@Column(name = "DataType", length = 3)
	private String dataType;

	@NotNull
	@Column(name = "creationDateTime")
	private Date creationTime;

	@Column(name = "visibility", length = 50)
	private String visibility;

	@Column(name = "action", length = 50)
	private String action;

	@Column(name = "maxLength", length = 4)
	private Long maxLength;

	@Column(name = "minLength", length = 4)
	private Long minLength;

	@Column(name = "regex", length = 200)
	private String regex;

	@Column(name = "placeHolder", length = 200)
	private String placeHolder;

	@Column(name = "mandatory", length = 1)
	private String mandatory;

	@Column(name = "controlLabel", length = 50)
	private String controlLabel;

	@Column(name = "displayText", length = 50)
	private String displayText;

	@Column(name = "valueText", length = 50)
	private String valueText;

}
