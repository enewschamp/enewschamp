package com.enewschamp.article.domain.common;

public enum ArticleRatingType {

	Rating1(1),
	Rating2(2),
	Rating3(3),
	Rating4(4),
	Rating5(5);
	
	private Integer value;
	
	private ArticleRatingType(Integer value) {

		this.value = 0;
	}

	public String toString() {

		return String.valueOf(this.value);
	}

	/**
	 * @param addressTypeStr
	 * @return
	 */
	public static ArticleRatingType fromValue(Integer value) {

		ArticleRatingType type = null;
		for (ArticleRatingType ratingType : ArticleRatingType.values()) {
			if(ratingType.toString().equals(String.valueOf(value))) {
				type = ratingType;
				break;
			}
		}
		return type;
	}
}
