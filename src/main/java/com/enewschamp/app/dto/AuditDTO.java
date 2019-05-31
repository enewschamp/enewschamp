package com.enewschamp.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javers.core.metamodel.object.InstanceId;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.domain.common.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({ "operatorId", "operationDateTime", "action", "objectName", "objectId", "commitId","changedFields" })
public class AuditDTO extends AbstractDTO {

	private static final long serialVersionUID = 8757664638986106614L;

	private String operatorId;

	private LocalDateTime operationDateTime;

	private OperationType action;

	private List<FieldChangeDTO> changedFields;
	
	private BigDecimal commitId;
	
	private JsonNode snapshot;
	
	@JsonIgnore
	private String objectName;
	
	@JsonIgnore
	private String objectId;
	
	private Map<String, List<AuditDTO>> childItems;
	
	@JsonIgnore
	private List<String> childItemPropertyNames;
	
	private Map<String, Object> additionalProperties;
	
	public AuditDTO() {
		
	}
	
	public void addChildObject(String propertyName, AuditDTO childObjectAudit) {
		if(childItems == null) {
			childItems = new HashMap<String, List<AuditDTO>>();
		}
		propertyName = propertyName + "Audit";
		if(childItems.get(propertyName) == null) {
			childItems.put(propertyName, new ArrayList<AuditDTO>());
		}
		childItems.get(propertyName).add(childObjectAudit);
	}
	
	public void addFieldChange(FieldChangeDTO fieldChange) {
		if(changedFields == null) {
			changedFields = new ArrayList<FieldChangeDTO>();
		}
		changedFields.add(fieldChange);
	}
	
	public void addChildItemPropertyName(String propertyName) {
		if(childItemPropertyNames == null) {
			childItemPropertyNames = new ArrayList<String>();
		}
		childItemPropertyNames.add(propertyName);
	}
	
	public void addAdditionalProperty(String property, Object value) {
		if(additionalProperties == null) {
			additionalProperties = new HashMap<String, Object>();
		}
		additionalProperties.put(property, value);
	}
	
	public void addObjectProperties(InstanceId instance, String idPropertyName) {
		this.objectName = instance.getTypeName();
		this.objectId = instance.getCdoId().toString();
		addAdditionalProperty(idPropertyName, this.objectId);
		
	}
	
}
