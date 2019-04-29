package com.enewschamp.article.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionNumber;

import com.enewschamp.common.BaseEntity;

import lombok.Data;

@Entity
@Table(name="NewsArticle")
@Audited
@AuditTable(value="NewsArticleAudit")
@Data
public class NewsArticle extends BaseEntity{	
	
	private static final long serialVersionUID = 4067120832023693933L;

	@Id
	@NotNull
	@GeneratedValue
    @RevisionNumber
	@Column(name = "NewsArticleID", length=10)
	private long newsArticleID = 0L;
	
	@Embedded
	private BaseNewsArticle baseNewsArticle;
	
	
}
