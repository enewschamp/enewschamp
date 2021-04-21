package com.enewschamp.domain.common;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.page.dto.ListOfValuesItem;

public enum MonthType {
	Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;

	public static List<ListOfValuesItem> getLOV() {
		List<ListOfValuesItem> arrayList = new ArrayList<ListOfValuesItem>();
		for (MonthType monthType : MonthType.values()) {
			ListOfValuesItem item = new ListOfValuesItem();
			item.setId(monthType.name());
			item.setName(monthType.name());
			arrayList.add(item);
		}
		return arrayList;
	}
}
