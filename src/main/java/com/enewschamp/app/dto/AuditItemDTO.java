package com.enewschamp.app.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuditItemDTO extends AbstractDTO{	
	
	private static final long serialVersionUID = 2145945760045182232L;

	private String operatorId;
	
	private LocalDateTime operationDateTime;

	Map<String, List<ChangeDTO>> changes;
}
