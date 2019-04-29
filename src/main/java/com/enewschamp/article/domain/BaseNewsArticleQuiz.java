package com.enewschamp.article.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Embeddable
@Audited
@MappedSuperclass
public class BaseNewsArticleQuiz {

	@NotNull
	@Column(name = "NewsArticleID", length=10)
	private long newsArticleID = 0L;
	
	@Column(name = "Question", length = 99)
	private String question;
	
	@NotNull
	@Column(name = "Opt1", length = 50)
	private String opt1;
	
	@NotNull
	@Column(name = "Opt2", length = 50)
	private String opt2;
	
	@Column(name = "Opt3", length = 50)
	private String opt3;
	
	@Column(name = "Opt4", length = 50)
	private String opt4;
	
	@NotNull
	@Column(name = "CorrectOpt", length=1)
	private int correctOpt = 0;
	@NotNull
	@Column(name = "RecordInUse", length = 1)
	private String recordInUse;	

	@Column(name = "AuthorID", length=10)
	protected String authorID;

	@Column(name = "AuthorDateTime")
	protected Timestamp authorDateTime = new Timestamp(System.currentTimeMillis());

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public Timestamp getAuthorDateTime() {
		return authorDateTime;
	}

	public void setAuthorDateTime(Timestamp authorDateTime) {
		this.authorDateTime = authorDateTime;
	}
	
	public long getNewsArticleID() {
		return newsArticleID;
	}

	public void setNewsArticleID(long newsArticleID) {
		this.newsArticleID = newsArticleID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOpt1() {
		return opt1;
	}

	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}

	public String getOpt2() {
		return opt2;
	}

	public void setOpt2(String opt2) {
		this.opt2 = opt2;
	}

	public String getOpt3() {
		return opt3;
	}

	public void setOpt3(String opt3) {
		this.opt3 = opt3;
	}

	public String getOpt4() {
		return opt4;
	}

	public void setOpt4(String opt4) {
		this.opt4 = opt4;
	}

	public int getCorrectOpt() {
		return correctOpt;
	}

	public void setCorrectOpt(int correctOpt) {
		this.correctOpt = correctOpt;
	}

	public String getRecordInUse() {
		return recordInUse;
	}

	public void setRecordInUse(String recordInUse) {
		this.recordInUse = recordInUse;
	}
}
