package com.enewschamp.app.dto;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionItemChangeDTO extends AbstractDTO {

	private static final long serialVersionUID = -6733586851015286933L;

	private CollectionItemChangeType changeType;

	private Object snapShot;
	private Object typeName;
	private Object key;
}
