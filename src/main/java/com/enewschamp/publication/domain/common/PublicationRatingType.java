package com.enewschamp.publication.domain.common;

public enum PublicationRatingType {

	Rating1(1), Rating2(2), Rating3(3), Rating4(4), Rating5(5);

	private Integer value;

	private PublicationRatingType(Integer value) {

		this.value = 0;
	}

	public String toString() {

		return String.valueOf(this.value);
	}

	/**
	 * @param addressTypeStr
	 * @return
	 */
	public static PublicationRatingType fromValue(Integer value) {

		PublicationRatingType type = null;
		for (PublicationRatingType ratingType : PublicationRatingType.values()) {
			if (ratingType.toString().equals(String.valueOf(value))) {
				type = ratingType;
				break;
			}
		}
		return type;
	}
}
