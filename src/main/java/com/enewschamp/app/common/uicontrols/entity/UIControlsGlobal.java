package com.enewschamp.app.common.uicontrols.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UIControlsGlobal", uniqueConstraints = { @UniqueConstraint(columnNames = { "controlName" }),
		@UniqueConstraint(columnNames = { "globalControlRef" }) })
public class UIControlsGlobal extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uicontrols_global_id_generator")
	@SequenceGenerator(name = "uicontrols_global_id_generator", sequenceName = "uicontrols_global_id_seq", allocationSize = 1)
	@Column(name = "uiControlGlobalId", updatable = false, nullable = false)
	private Long uiControlGlobalId;

	@NotNull
	@Column(name = "controlName", length = 50)
	private String controlName;

	@NotNull
	@Column(name = "controlType", length = 50)
	private String controlType;

	@Column(name = "controlLabel", length = 500)
	private String controlLabel;

	@Column(name = "dataType", length = 20)
	private String dataType;

	@Column(name = "globalControlRef", length = 50)
	private String globalControlRef;

	@Column(name = "defaultValue", length = 500)
	private String defaultValue;

	@Column(name = "placeHolder", length = 500)
	private String placeHolder;

	@Column(name = "helpText", length = 4000)
	private String helpText;

	@Column(name = "icon", length = 50)
	private String icon;

	@Column(name = "iconType", length = 50)
	private String iconType;

	@Column(name = "keyboard", length = 50)
	private String keyboard;

	@Column(name = "multiLine", length = 1)
	private String multiLine;

	@Column(name = "minLength", length = 4)
	private String minLength;

	@Column(name = "maxLength", length = 4)
	private String maxLength;

	@Column(name = "height", length = 100)
	private String height;

	@Column(name = "width", length = 100)
	private String width;

	@Column(name = "visibility", length = 20)
	private String visibility;

	@Column(name = "mandatory", length = 500)
	private String mandatory;

	@Column(name = "regex", length = 500)
	private String regex;

	@Column(name = "action", length = 50)
	private String action;

	@Column(name = "successMessage", length = 500)
	private String successMessage;

	@Column(name = "errorMessage", length = 500)
	private String errorMessage;

	@Column(name = "confirmationMessage", length = 500)
	private String confirmationMessage;

	@Column(name = "isPremiumFeature", length = 1)
	private String isPremiumFeature;

	@Column(name = "unavailableMessage", length = 500)
	private String unavailableMessage;
}
