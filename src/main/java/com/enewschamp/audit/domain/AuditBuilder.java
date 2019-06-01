package com.enewschamp.audit.domain;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.javers.core.Changes;
import org.javers.core.ChangesByCommit;
import org.javers.core.ChangesByObject;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ElementValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.CdoSnapshotState;
import org.javers.core.metamodel.object.InstanceId;
import org.javers.core.metamodel.object.SnapshotType;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.dto.AuditDTO;
import com.enewschamp.app.dto.FieldChangeDTO;
import com.enewschamp.domain.common.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditBuilder  {

	private ObjectMapper objectMapper;
	
	private AuditService auditService;

	private Object parentObject;
	
	private String parentObjectId;
	
	private List<Object> childObjects;
	
	private Map<String, String> childCollectionProperties;
	
	private List<CommitId> processedCommitIds;
	
	private Map<String, String> idPropertyNames;
	
	private EnewschampApplicationProperties appConfig;
	
	private AuditBuilder(AuditService auditService, ObjectMapper objectMapper, EnewschampApplicationProperties appConfig) {
		this.auditService = auditService;
		this.objectMapper = objectMapper;
		this.appConfig = appConfig;
	}
	
	public static AuditBuilder getInstance(AuditService auditService, ObjectMapper objectMapper, EnewschampApplicationProperties appConfig) {
		return new AuditBuilder(auditService, objectMapper, appConfig);
	}
	
	public AuditBuilder forParentObject(Object parentObjectInstance) {
		this.parentObject = parentObjectInstance;
		deriveCollectionProperties(this.parentObject.getClass());
		return this;
	}
	
	public AuditBuilder forChildObject(Object chidObjectInstance) {
		if(childObjects == null) {
			childObjects = new ArrayList<Object>();
		}
		childObjects.add(chidObjectInstance);
		return this;
	}
	
	private void deriveCollectionProperties(Class objectClass) {
		Field[] fields = objectClass.getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Type type = field.getType();
				Class<?> clazz = (Class<?>) type;
				if (clazz == List.class) {
					ParameterizedType listType = (ParameterizedType) field.getGenericType();
					Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
					addChildCollectionProperty(listClass.getName(), field.getName());
					try {
						deriveCollectionProperties(Class.forName(listClass.getName()));
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				}
				
				if(field.getAnnotation(Id.class) != null) {
					addIdPropertyNames(objectClass.getName(), field.getName());
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} 
		}
	}
	
	private void addIdPropertyNames(String typeName, String idPropertyName) {
		if(idPropertyNames == null) {
			idPropertyNames = new HashMap<String, String>();
		}
		idPropertyNames.put(typeName, idPropertyName);
	}

	private void addChildCollectionProperty(String collectionTypeName, String propertyName) {
		if(childCollectionProperties == null) {
			childCollectionProperties = new HashMap<String, String>();
		}
		childCollectionProperties.put(collectionTypeName, propertyName);
	}
	
	private String getChildCollectionPropertyName(String typeName) {
		return childCollectionProperties.get(typeName);
	}
	
	private String getIdPropertyName(String typeName) {
		return idPropertyNames.get(typeName);
	}
	
	public String build() {
		
		List<AuditDTO> allAudit = new ArrayList<AuditDTO>();
		processedCommitIds = new ArrayList<CommitId>();
		
		// Get audit for all parent objects. While fetching audit for parent object, audit for Added and Removed Child object also comes as part of
		// collection change audit. Same applies for all levels of object hierarchy.
		allAudit.addAll(getAuditForParentObject(this.parentObject));
		for(Object childObject: childObjects) {
			allAudit.addAll(getAuditForChildObject(childObject));
		}
		return buildOutput(allAudit);
	}
	
	private List<AuditDTO> getAuditForParentObject(Object entityInstance) {
		List<AuditDTO> allAudit = new ArrayList<AuditDTO>();
		
		// Build for new object addition
		allAudit.add(getAuditForNewObjectCreation(entityInstance));
				
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		Changes changes = auditService.getEntityChangesByCriteria(entityInstance, queryCriteria);
		allAudit.addAll(getAuditForChanges(changes));

		return allAudit;
	}
	
	private AuditDTO getAuditForNewObjectCreation(Object entityInstance) {
		AuditDTO newObjectAuditDTO = new AuditDTO();
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		queryCriteria.setVersion(Long.valueOf(1));
		queryCriteria.setSnapshotType(SnapshotType.INITIAL);
		queryCriteria.setWithNewObjectChanges(true);
		Changes changes = auditService.getEntityChangesByCriteria(entityInstance, queryCriteria);
		
		changes.forEach(change -> {
			
			if(change instanceof NewObject) {
				
				NewObject newObjectChange = (NewObject) change;
				CommitMetadata commitMetadata = newObjectChange.getCommitMetadata().get();
				InstanceId instanceId = (InstanceId)newObjectChange.getAffectedGlobalId();
				newObjectAuditDTO.addObjectProperties(instanceId, getIdPropertyName(instanceId.getTypeName()));
				newObjectAuditDTO.addCommitInfo(commitMetadata);
				newObjectAuditDTO.setAction(OperationType.Add);
				
				System.out.println("Processing audit for object: " + instanceId.getTypeName() + " Commit Id: " + commitMetadata.getId());

				AuditQueryCriteria snapshotQueryCriteria = new AuditQueryCriteria();
				snapshotQueryCriteria.setCommitId(commitMetadata.getId());
				snapshotQueryCriteria.setSnapshotType(SnapshotType.INITIAL);
				snapshotQueryCriteria.setVersion(Long.valueOf(1));
				CdoSnapshot snapshot = getEntitySnapshot(instanceId, snapshotQueryCriteria);
				newObjectAuditDTO.setSnapshot(buildSnapShotNode(snapshot));
				
				getChildAuditForSameCommitId(commitMetadata, newObjectAuditDTO, true);
				
				processedCommitIds.add(commitMetadata.getId());
			}
		});
		
		return newObjectAuditDTO;
	}
	
	private List<AuditDTO> getAuditForChildObject(Object entityInstance) {
		List<AuditDTO> allAudit = new ArrayList<AuditDTO>();
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		queryCriteria.setSnapshotType(SnapshotType.UPDATE);
		Changes changes = auditService.getEntityChangesByCriteria(entityInstance, queryCriteria);
		allAudit =  getAuditForChanges(changes);
		return allAudit;
	}
	
	private List<AuditDTO> getAuditForChanges(Changes changes) {
		List<ChangesByCommit> allChangesByCommit = new ArrayList<ChangesByCommit>();

		if(changes != null && changes.size() > 0) {
			allChangesByCommit.addAll(changes.groupByCommit());
		}
		
		List<AuditDTO> audits = new ArrayList<AuditDTO>();
		
		// Process commit id wise changes
		allChangesByCommit.forEach(byCommit -> {

			CommitMetadata commitMetadata = byCommit.getCommit();
			if(processedCommitIds.contains(commitMetadata.getId())) {
				return;
			}
			AuditDTO auditDTO = new AuditDTO();

			processedCommitIds.add(commitMetadata.getId());
			
			byCommit.groupByObject().forEach(byObject -> {
				InstanceId instanceId = (InstanceId) byObject.getGlobalId();
				
				auditDTO.addObjectProperties(instanceId, getIdPropertyName(instanceId.getTypeName()));
				
				System.out.println("Processing audit for object: " + instanceId.getTypeName() + " Commit Id: " + commitMetadata.getId());
				
				AuditQueryCriteria snapshotQueryCriteria = new AuditQueryCriteria();
				snapshotQueryCriteria.setCommitId(commitMetadata.getId());
				CdoSnapshot snapshot = getEntitySnapshot((InstanceId) byObject.getGlobalId(), snapshotQueryCriteria);
				
				if(snapshot != null) {
					auditDTO.setSnapshot(buildSnapShotNode(snapshot));
					auditDTO.setAction(OperationType.fromSnapShotType(snapshot.getType()));
				}
				
				if(this.parentObject.getClass().getName().equals(instanceId.getTypeName())) {
					
					auditDTO.addCommitInfo(commitMetadata);
					
					this.parentObjectId = instanceId.getCdoId().toString();
					buildParentChangeAuditDTO(commitMetadata, auditDTO, byObject, instanceId.getTypeName());
					audits.add(auditDTO);
				} else {
					AuditDTO parentAuditDTO = new AuditDTO();
					parentAuditDTO.addCommitInfo(commitMetadata);
					parentAuditDTO.setAction(auditDTO.getAction());
					parentAuditDTO.setObjectName(this.parentObject.getClass().getName());
					parentAuditDTO.addAdditionalProperty(getIdPropertyName(this.parentObject.getClass().getName()), this.parentObjectId);
					
					parentAuditDTO.addChildObject(getChildCollectionPropertyName(byObject.getGlobalId().getTypeName()), auditDTO);
					
					buildChildChangeAuditDTO(commitMetadata, auditDTO, byObject, instanceId.getTypeName());
					audits.add(parentAuditDTO);
				}
		        
		    });
		});
		return audits;
	}

	private void buildParentChangeAuditDTO(CommitMetadata commitMetadata, AuditDTO auditDTO, ChangesByObject byObject,
			String typeName) {

		byObject.get().forEach(change -> {
			// Value change
			if (change instanceof ValueChange) {
				handleValueChange(auditDTO, (ValueChange) change);
			}
			// Child object change
			if (change instanceof ListChange) {
				ListChange listChange = (ListChange) change;
				List<ContainerElementChange> collectionChanges = listChange.getChanges();
				auditDTO.addChildItemPropertyName(listChange.getPropertyName());

				collectionChanges.forEach(collectionChange -> {
					if (collectionChange instanceof ValueAdded) {
						ValueAdded itemAdded = (ValueAdded) collectionChange;
						handleItemAddition(commitMetadata, auditDTO, listChange.getPropertyName(), ((InstanceId) itemAdded.getValue()));
					}
					if (collectionChange instanceof ValueRemoved) {
						ValueRemoved itemRemoved = (ValueRemoved) collectionChange;
						handleChildItemRemoval(auditDTO, listChange.getPropertyName(), ((InstanceId) itemRemoved.getValue()));
					}
					if (collectionChange instanceof ElementValueChange) {
						handleChildElementValueChange(commitMetadata, auditDTO, listChange.getPropertyName(), (ElementValueChange) collectionChange);
					}
				});
			}
		});
		
		// For the same commit id, check if there are any modifications done to child objects
		if(this.parentObject.getClass().getName().equals(typeName)) {
			getChildAuditForSameCommitId(commitMetadata, auditDTO, false);
		}
		
	}
	
	private void getChildAuditForSameCommitId(CommitMetadata commitMetadata, AuditDTO auditDTO, boolean isForNewObject) {
		
		System.out.println("Checking for child audit records for commit id: " + commitMetadata.getId().valueAsNumber());
		if(childCollectionProperties != null) {
			childCollectionProperties.forEach((propertyTypeName, propertyName) -> {
				AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
				queryCriteria.setCommitId(commitMetadata.getId());
				queryCriteria.setObjectName(propertyTypeName);
				queryCriteria.setWithNewObjectChanges(isForNewObject);
				Changes changes = auditService.getEntityChangesByCriteria(null, queryCriteria);
					
				changes.groupByObject().forEach(byChildObject -> {
					byChildObject.get().forEach(change -> {
						if (change instanceof ValueChange && !isForNewObject) {
							handleItemModification(commitMetadata, auditDTO, 
												   getChildCollectionPropertyName(change.getAffectedGlobalId().getTypeName()), 
												   (InstanceId)change.getAffectedGlobalId(), 
												   (ValueChange) change);
						}
						
						if (change instanceof NewObject && isForNewObject) {
							handleItemAddition(commitMetadata, auditDTO, 
											   getChildCollectionPropertyName(change.getAffectedGlobalId().getTypeName()), 
											   (InstanceId) change.getAffectedGlobalId());	
						}
					});
				});
			});
		}
	}

	private void buildChildChangeAuditDTO(CommitMetadata commitMetadata, 
										  AuditDTO childAuditDTO, 
										  ChangesByObject byObject,
										  String typeName) {
		byObject.get().forEach(change -> {
			if (change instanceof ValueChange) {
				handleValueChange(childAuditDTO, (ValueChange) change);
			}
		});
	}

	private void handleValueChange(AuditDTO auditDTO, ValueChange valueChange) {
		FieldChangeDTO fieldChange = new FieldChangeDTO(valueChange.getPropertyNameWithPath(), valueChange.getLeft(),
				valueChange.getRight());
		auditDTO.addFieldChange(fieldChange);
	}

	private void handleItemAddition(CommitMetadata commitMetadata, 
								    AuditDTO auditDTO, 
								    String propertyName,
								    InstanceId addedItemInstanceDetails) {

		AuditDTO childObjectAudit = new AuditDTO();
		childObjectAudit.setAction(OperationType.Add);
		childObjectAudit.addObjectProperties(addedItemInstanceDetails, getIdPropertyName(addedItemInstanceDetails.getTypeName()));
		AuditQueryCriteria snapshotQueryCriteria = new AuditQueryCriteria();
		snapshotQueryCriteria.setCommitId(commitMetadata.getId());
		snapshotQueryCriteria.setSnapshotType(SnapshotType.INITIAL);
		CdoSnapshot snapShot = getEntitySnapshot(addedItemInstanceDetails, snapshotQueryCriteria);
		childObjectAudit.setSnapshot(buildSnapShotNode(snapShot));
		auditDTO.addChildObject(propertyName, childObjectAudit);
	}

	private void handleItemModification(CommitMetadata commitMetadata, AuditDTO auditDTO, String propertyName,
			InstanceId modifiedItemInstanceDetails, ValueChange valueChange) {

		AuditDTO childObjectAudit = new AuditDTO();
		childObjectAudit.setAction(OperationType.Modify);
		childObjectAudit.addObjectProperties(modifiedItemInstanceDetails, getIdPropertyName(modifiedItemInstanceDetails.getTypeName()));
		handleValueChange(childObjectAudit, valueChange);
		
		AuditQueryCriteria snapshotQueryCriteria = new AuditQueryCriteria();
		snapshotQueryCriteria.setCommitId(commitMetadata.getId());
		snapshotQueryCriteria.setSnapshotType(SnapshotType.UPDATE);
		snapshotQueryCriteria.setCommitId(commitMetadata.getId());
		CdoSnapshot snapShot = getEntitySnapshot(modifiedItemInstanceDetails, snapshotQueryCriteria);
		childObjectAudit.setSnapshot(buildSnapShotNode(snapShot));
		
		auditDTO.addChildObject(propertyName, childObjectAudit);
	}
	
	private void handleChildItemRemoval(AuditDTO auditDTO, String propertyName, InstanceId removedItemInstanceDetails) {
		AuditDTO childObjectAudit = new AuditDTO();
		childObjectAudit.setAction(OperationType.Remove);
		childObjectAudit.addObjectProperties(removedItemInstanceDetails, getIdPropertyName(removedItemInstanceDetails.getTypeName()));
		AuditQueryCriteria snapshotQueryCriteria = new AuditQueryCriteria();
		snapshotQueryCriteria.setSnapshotType(SnapshotType.TERMINAL);
		CdoSnapshot terminalSnapShot = getEntitySnapshot(removedItemInstanceDetails, snapshotQueryCriteria);
		
		// When item is removed from the child item list, terminal snapshot is empty hence snapshot of previous version has been loaded here
		if(terminalSnapShot != null) {
			snapshotQueryCriteria = new AuditQueryCriteria();
			snapshotQueryCriteria.setVersion(terminalSnapShot.getVersion() - 1);
			CdoSnapshot snapShot = getEntitySnapshot(removedItemInstanceDetails, snapshotQueryCriteria);
			childObjectAudit.setSnapshot(buildSnapShotNode(snapShot));
		}
		
		auditDTO.addChildObject(propertyName, childObjectAudit);
	}
	
	private void handleChildElementValueChange(CommitMetadata commitMetadata, AuditDTO auditDTO, String propertyName,
			ElementValueChange itemExchanged) {

		// In case of element change, left value is the removed item
		InstanceId removedItemInstanceDetails = (InstanceId) itemExchanged.getLeftValue();
		handleChildItemRemoval(auditDTO, propertyName, removedItemInstanceDetails);

		// In case of element change, right value is added item
		InstanceId addedItemInstanceDetails = (InstanceId) itemExchanged.getRightValue();
		handleItemAddition(commitMetadata, auditDTO, propertyName, addedItemInstanceDetails);
	}

	private JsonNode buildSnapShotNode(CdoSnapshot snapshot) {
		JsonNode node = null;
		if (snapshot != null) {
			CdoSnapshotState state = snapshot.getState();
			Map<String, Object> valueMap = new HashMap<String, Object>();
			for (String propertyName : state.getPropertyNames()) {
				Object value = state.getPropertyValue(propertyName);
				
				if(value instanceof List) {
					List<InstanceId> valueList = (ArrayList<InstanceId>) value;
					List<Object> newValueList = new ArrayList<Object>();
					for(InstanceId instanceId: valueList) {
						Map<String, Object> modifiedMap = new HashMap<String, Object>();
						//modifiedMap.put("objectName", instanceId.getTypeName());
						modifiedMap.put(getIdPropertyName(instanceId.getTypeName()), instanceId.getCdoId().toString());
						newValueList.add(modifiedMap);
					}
					valueMap.put(propertyName, newValueList);
				} else {
					valueMap.put(propertyName, state.getPropertyValue(propertyName));
				}
			}
			node = objectMapper.convertValue(valueMap, JsonNode.class);
		}
		return node;
	}
	

	private String buildOutput(List<AuditDTO> auditList) {
		String response = null;
		if (auditList == null) {
			return response;
		}
		
		//Sort the audit list by commit id in descending order
		auditList.sort((AuditDTO audit1, AuditDTO audit2) -> audit2.getCommitId().compareTo(audit1.getCommitId()));
		
		// Create output
		List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
		for (AuditDTO audit : auditList) {
			
			Map<String, Object> map = objectMapper.convertValue(audit, new TypeReference<Map<String, Object>>() {
			});

			Map<String, Object> childItems = (HashMap<String, Object>) map.get("childItems");
			if(childItems != null) {
				
				childItems.forEach((collectionPropertyName, auditObjectList) -> {
					List<Object> auditObjects = (List<Object>) auditObjectList;
					auditObjects.forEach(auditDTO -> {
						injectAdditionalProperties((Map<String, Object>) auditDTO);
					});
				});
				
				map.putAll(childItems);
				map.remove("childItems");
			}

			injectAdditionalProperties(map);
			
			outputList.add(map);
		}

		JsonNode output = objectMapper.convertValue(outputList, JsonNode.class);
		try {
			response = objectMapper.writeValueAsString(output);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	private void injectAdditionalProperties(Map<String, Object> auditMap) {
		
		if(auditMap.get("additionalProperties") != null) {
			Map<String, Object> additionalPropertiesMap = (Map<String, Object>) auditMap.get("additionalProperties");
			additionalPropertiesMap.forEach((propertyName, propertyValue) -> {
				auditMap.put(propertyName, propertyValue);
			});
			auditMap.remove("additionalProperties");
		}
	}
	
	private CdoSnapshot getEntitySnapshot(InstanceId instanceId, AuditQueryCriteria snapshotQuery) {
		return auditService.getEntitySnapshot(instanceId, snapshotQuery);
	}
	
}
