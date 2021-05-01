package com.enewschamp.domain.common;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.page.dto.ListOfValuesItem;

public enum YesNoType {
	Y, N;

	public static List<ListOfValuesItem> getLOV() {
		List<ListOfValuesItem> arrayList = new ArrayList<ListOfValuesItem>();
		for (YesNoType dayType : YesNoType.values()) {
			ListOfValuesItem item = new ListOfValuesItem();
			item.setId(dayType.name());
			item.setName(dayType.name());
			arrayList.add(item);
		}

		return arrayList;
	}
}
