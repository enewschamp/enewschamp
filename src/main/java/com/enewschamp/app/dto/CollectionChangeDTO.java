package com.enewschamp.app.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionChangeDTO extends ChangeDTO {

	private static final long serialVersionUID = 6362619072643856495L;

	private List<CollectionItemChangeDTO> itemChanges;
}
