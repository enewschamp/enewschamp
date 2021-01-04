package com.enewschamp.article.domain.common;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.page.dto.ListOfValuesItem;

public enum ArticleType {

	NEWSARTICLE("News Article"), NEWSEVENT("News Event");

	private String name;

	private ArticleType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<ListOfValuesItem> getLOV() {
		List<ListOfValuesItem> arrayList = new ArrayList<ListOfValuesItem>();
		for (ArticleType articleType : ArticleType.values()) {
			ListOfValuesItem item = new ListOfValuesItem();
			item.setId(articleType.toString());
			item.setName(articleType.getName());
			arrayList.add(item);
		}
		return arrayList;
	}
}
