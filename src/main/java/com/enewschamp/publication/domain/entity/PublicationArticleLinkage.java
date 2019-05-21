package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="PublicationArticles")
public class PublicationArticleLinkage  {

	private static final long serialVersionUID = -6183606960645724096L;
	
	@EmbeddedId
	private PublicationArticleLinkageKey publicationArticleLinkageKey; 
	
	@NotNull
	@Column(name = "Sequence")
	private int sequence;
	
}
