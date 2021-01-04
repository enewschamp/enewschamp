package com.enewschamp.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.publication.domain.common.LOVProjection;

public abstract class AbstractDomainService {

	protected List<ListOfValuesItem> toListOfValuesItems(List<LOVProjection> items) {

		List<ListOfValuesItem> lovItems = new ArrayList<ListOfValuesItem>();
		for (LOVProjection item : items) {
			ListOfValuesItem lovItem = new ListOfValuesItem();
			lovItem.setName(item.getName());
			lovItem.setId(item.getId());
			lovItem.setIsActive(item.getIsActive());
			lovItems.add(lovItem);
		}
		return lovItems;
	}
}
