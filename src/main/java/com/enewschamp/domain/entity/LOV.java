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
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "LOV")
public class LOV extends BaseEntity {

	private static final long serialVersionUID = 1449365631682209802L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lov_id_generator")
	@SequenceGenerator(name = "lov_id_generator", sequenceName = "lov_id_seq", allocationSize = 1)
	@Column(name = "LovId", updatable = false, nullable = false)
	private long lovId = 0L;

	@NotNull
	@Column(name = "Type", length = 20)
	private String type;
	
	@NotNull
	@Column(name = "NameId", length = ForeignKeyColumnLength.MultiLanguageId)
	private String nameId;

	@NotNull
	@Column(name = "Description", length = 50)
	private String description;
}
