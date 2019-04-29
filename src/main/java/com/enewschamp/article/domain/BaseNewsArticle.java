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
public class BaseNewsArticle {
	
	@NotNull
	@Column(name = "NewsArticleGroupID", length=10)
	private long newsArticleGroupID = 0L;
	
	@NotNull
	@Column(name = "ReadingLevel", length=1)
	private int readingLevel = 0;
	
	@NotNull
	@Column(name = "GenreID", length = 10)
	private long genreID;
	
	@NotNull
	@Column(name = "Headline", length = 50)
	private String headline;
	
	@NotNull
	@Column(name = "Content", length = 2000)
	private String content;
	
	@NotNull
	@Column(name = "Credits", length = 100)
	private String credits;
	
	@NotNull
	@Column(name = "ImagePath", length = 200)
	private String imagePath;
	
	@Column(name = "HashTags", length = 999)
	private String hashTags;
	@NotNull
	@Column(name = "RecordInUse", length = 1)
	private String recordInUse;	
	
	@Column(name = "LikeLCount")
	private int likeLCount = 0;
	
	@Column(name = "LikeHCount")
	private int likeHCount = 0;
	
	@Column(name = "LikeOCount")
	private int likeOCount = 0;
	
	@Column(name = "LikeWCount")
	private int likeWCount = 0;
	
	@Column(name = "LikeSCount")
	private int likeSCount = 0;

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
	
	public long getNewsArticleGroupID() {
		return newsArticleGroupID;
	}

	public void setNewsArticleGroupID(long newsArticleGroupID) {
		this.newsArticleGroupID = newsArticleGroupID;
	}

	public int getReadingLevel() {
		return readingLevel;
	}

	public void setReadingLevel(int readingLevel) {
		this.readingLevel = readingLevel;
	}

	public long getGenreID() {
		return genreID;
	}

	public void setGenreID(long genreID) {
		this.genreID = genreID;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getHashTags() {
		return hashTags;
	}

	public void setHashTags(String hashTags) {
		this.hashTags = hashTags;
	}

	public String getRecordInUse() {
		return recordInUse;
	}

	public void setRecordInUse(String recordInUse) {
		this.recordInUse = recordInUse;
	}

	public int getLikeLCount() {
		return likeLCount;
	}

	public void setLikeLCount(int likeLCount) {
		this.likeLCount = likeLCount;
	}

	public int getLikeHCount() {
		return likeHCount;
	}

	public void setLikeHCount(int likeHCount) {
		this.likeHCount = likeHCount;
	}

	public int getLikeOCount() {
		return likeOCount;
	}

	public void setLikeOCount(int likeOCount) {
		this.likeOCount = likeOCount;
	}

	public int getLikeWCount() {
		return likeWCount;
	}

	public void setLikeWCount(int likeWCount) {
		this.likeWCount = likeWCount;
	}

	public int getLikeSCount() {
		return likeSCount;
	}

	public void setLikeSCount(int likeSCount) {
		this.likeSCount = likeSCount;
	}
	
	
	
}
