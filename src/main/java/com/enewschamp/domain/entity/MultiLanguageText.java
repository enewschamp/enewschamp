package com.enewschamp.domain.entity;

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
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="MultiLanguageText")
public class MultiLanguageText extends BaseEntity {	
	
	private static final long serialVersionUID = 5418795295175530388L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "multi_lang_text_id_generator")
	@SequenceGenerator(name="multi_lang_text_id_generator", sequenceName = "lang_text_id_seq", allocationSize=1)
	@Column(name = "MultiLanguageTextId", updatable = false, nullable = false)
	private long multiLanguageTextId = 0L;
	
	@NotNull
	@Column(name = "Locale", length=5)
	private String locale;

	@NotNull
	@Column(name = "EntityName", length=50)
	private String entityName;
	
	@NotNull
	@Column(name = "EntityColumn", length=50)
	private String entityColumn;
	
	@NotNull
	@Column(name = "Text", length=200)
	private String text;
	
}
