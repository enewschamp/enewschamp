package com.enewschamp.article.domain.common;

public enum ArticleImageType {

	Thumbnail,
	Mobile,
	Desktop,
	IPad;
	
	public static ArticleImageType fromValue(String status) {
		for(ArticleImageType statusType : ArticleImageType.values()) {
			if(statusType.toString().equals(status)) {
				return statusType;
			}
		}
		return null;
	}
}

