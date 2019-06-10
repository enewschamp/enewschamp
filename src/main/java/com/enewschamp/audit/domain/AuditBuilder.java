package com.enewschamp.audit.domain;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ElementValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.CdoSnapshotState;
import org.javers.core.metamodel.object.InstanceId;
import org.javers.core.metamodel.object.SnapshotType;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.dto.AuditDTO;
import com.enewschamp.app.dto.FieldChangeDTO;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.domain.common.IEntity;
import com.enewschamp.domain.common.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditBuilder  {

	private ObjectMapper objectMapper;
	
	private AuditService auditService;

	private IEntity parentObject;
	
	private String parentObjectId;
	
	private List<IEntity> childObjects;
	
	private Map<String, String> childCollectionProperties;
	
	private List<CommitId> processedCommitIds;
	
	private Map<String, String> idPropertyNames;
	
	private EnewschampApplicationProperties appConfig;
	
	private Map<BigDecimal, AuditDTO> commitWiseParentAudit;
	
	private String propertyName;
	
	private AuditBuilder(AuditService auditService, ObjectMapper objectMapper, EnewschampApplicationProperties appConfig) {
		this.auditService = auditService;
		this.objectMapper = objectMapper;
		this.appConfig = appConfig;
	}
	
	public static AuditBuilder getInstance(AuditService auditService, ObjectMapper objectMapper, EnewschampApplicationProperties appConfig) {
		return new AuditBuilder(auditService, objectMapper, appConfig);
	}
	
	public AuditBuilder forParentObject(IEntity parentObjectInstance) {
		this.parentObject = parentObjectInstance;
		deriveCollectionProperties(this.parentObject.getClass());
		return this;
	}
	
	public AuditBuilder forChildObject(IEntity chidObjectInstance) {
		if(childObjects == null) {
			childObjects = new ArrayList<IEntity>();
		}
		childObjects.add(chidObjectInstance);
		return this;
	}
	
	public AuditBuilder forProperty(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}
	
	private void addCommitWiseParentAudit(List<AuditDTO> audits) {
		if(commitWiseParentAudit == null) {
			commitWiseParentAudit = new HashMap<BigDecimal, AuditDTO>();
		}
		audits.forEach(audit -> {
			commitWiseParentAudit.put(audit.getCommitId(), audit);
		});
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
		allAudit.addAll(getAuditForObject(this.parentObject));
		addCommitWiseParentAudit(allAudit);
		
		if(childObjects != null) {
			for(Object childObject: childObjects) {
				allAudit.addAll(getAuditForObject(childObject));
			}
		}
		return buildOutput(allAudit);
	}
	
	private List<AuditDTO> getAuditForObject(Object entityInstance) {
		List<AuditDTO> allAudit = new ArrayList<AuditDTO>();
		
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		queryCriteria.setWithNewObjectChanges(true);
		Changes changes = auditService.getEntityChangesByCriteria(entityInstance, queryCriteria);
		allAudit.addAll(getAuditForChanges(changes));

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
//			if(processedCommitIds.contains(commitMetadata.getId())) {
//				return;
//			}

			processedCommitIds.add(commitMetadata.getId());
			
			byCommit.groupByObject().forEach(byObject -> {
				AuditDTO auditDTO = new AuditDTO();
				InstanceId instanceId = (InstanceId) byObject.getGlobalId();
				
				auditDTO.addObjectProperties(instanceId, getIdPropertyName(instanceId.getTypeName()));
				
				printDebugLine("Processing audit for object: " + instanceId.getTypeName() + " Commit Id: " + commitMetadata.getId());
				
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
					AuditDTO parentAuditDTO = getParentAuditForCommitId(commitMetadata.getId().valueAsNumber());
					if(parentAuditDTO == null) {
						parentAuditDTO = new AuditDTO();
						parentAuditDTO.addCommitInfo(commitMetadata);
						parentAuditDTO.setAction(auditDTO.getAction());
						parentAuditDTO.setObjectName(this.parentObject.getClass().getName());
						parentAuditDTO.addAdditionalProperty(getIdPropertyName(this.parentObject.getClass().getName()), this.parentObjectId);
						audits.add(parentAuditDTO);
					}
					
					parentAuditDTO.addChildObject(getChildCollectionPropertyName(byObject.getGlobalId().getTypeName()), auditDTO);
					
					if(!auditDTO.getAction().equals(OperationType.Add)) {
						buildChildChangeAuditDTO(commitMetadata, auditDTO, byObject, instanceId.getTypeName());
					}
				}
		        
		    });
		});
		return audits;
	}

	private void buildParentChangeAuditDTO(CommitMetadata commitMetadata, AuditDTO auditDTO, ChangesByObject byObject,
			String typeName) {
		
		if(!auditDTO.getAction().equals(OperationType.Add)) {
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
					
					// In case of collection change, only removed objects are considered here as different commit id is created for object removal
					// Added objects get considered as part of getChildAuditForSameParentCommitId method call which is made at the end of this method
					collectionChanges.forEach(collectionChange -> {
						if (collectionChange instanceof ValueRemoved) {
							ValueRemoved itemRemoved = (ValueRemoved) collectionChange;
							if(childObjectExists(((InstanceId) itemRemoved.getValue()))) {
								return;
							}
							handleChildItemRemoval(auditDTO, listChange.getPropertyName(), ((InstanceId) itemRemoved.getValue()));
						}
						if (collectionChange instanceof ElementValueChange) {
							// In case of element change, left value is the removed item
							ElementValueChange itemExchanged = (ElementValueChange) collectionChange;
							InstanceId removedItemInstanceDetails = (InstanceId) itemExchanged.getLeftValue();
							if(childObjectExists(removedItemInstanceDetails)) {
								return;
							}
							handleChildItemRemoval(auditDTO, listChange.getPropertyName(), removedItemInstanceDetails);
						}
					});
				}
							
			});
		}
		
		// For the same commit id, check if there are any modifications done to child objects
		getChildAuditForSameParentCommitId(commitMetadata, auditDTO);
	}
	
	private void buildChildChangeAuditDTO(CommitMetadata commitMetadata, AuditDTO auditDTO, ChangesByObject byObject,
			String typeName) {
		byObject.get().forEach(change -> {
			// Value change
			if (change instanceof ValueChange) {
				handleValueChange(auditDTO, (ValueChange) change);
			}
		});
	}
	
	private void getChildAuditForSameParentCommitId(CommitMetadata commitMetadata, AuditDTO auditDTO) {
		
		printDebugLine("Checking for child audit records for commit id: " + commitMetadata.getId().valueAsNumber());
		if(childCollectionProperties != null) {
			childCollectionProperties.forEach((propertyTypeName, propertyName) -> {
				AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
				queryCriteria.setCommitId(commitMetadata.getId());
				queryCriteria.setObjectName(propertyTypeName);
				queryCriteria.setWithNewObjectChanges(true);
				Changes changes = auditService.getEntityChangesByCriteria(null, queryCriteria);
					
				changes.groupByObject().forEach(byChildObject -> {
					
					//Ignore the changes for those child objects which are still present
					InstanceId objectInstance = (InstanceId)byChildObject.getGlobalId();
					if(childObjectExists(objectInstance)) {
						System.out.println("Skipping audit for " + objectInstance.getTypeName() + "#" + objectInstance.getCdoId());
						return;
					}
					if(byChildObject.getNewObjects() != null && byChildObject.getNewObjects().size() > 0) {
						Change change = byChildObject.getNewObjects().get(0);
						handleItemAddition(commitMetadata, 
								   		   auditDTO, 
								   		   propertyName, 
								   		   (InstanceId) change.getAffectedGlobalId());
						return;
					}
					byChildObject.get().forEach(change -> {
						InstanceId instance = (InstanceId) change.getAffectedGlobalId();
						if (change instanceof ValueChange) {
							handleItemModification(commitMetadata, 
												   auditDTO, 
												   propertyName, 
												   instance, 
												   (ValueChange) change);
						}
						if (change instanceof ObjectRemoved) {
							handleChildItemRemoval(auditDTO, propertyName, instance);
						}
					});
				});
			});
		}
	}
	
	private boolean childObjectExists(InstanceId instance) {
		if(this.childObjects != null && this.childObjects.size() > 0) {
			for(IEntity childObject: this.childObjects) {
				String key = null;
				if(childObject != null) {
					key = childObject.getKeyAsString();
				}
				String instanceKey = String.valueOf(instance.getCdoId());
				if(key != null && key.equals(instanceKey)) {
					return true;
				}
			}
		}

		return false;
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

		System.out.println("Adding item for " + addedItemInstanceDetails.getTypeName() + "#" + addedItemInstanceDetails.getCdoId());
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

	private void handleItemModification(CommitMetadata commitMetadata, 
										AuditDTO auditDTO, 
										String propertyName,
										InstanceId modifiedItemInstanceDetails, 
										ValueChange valueChange) {

		System.out.println("Modifying item for " + modifiedItemInstanceDetails.getTypeName() + "#" + modifiedItemInstanceDetails.getCdoId());
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
		System.out.println("Removing item for " + removedItemInstanceDetails.getTypeName() + "#" + removedItemInstanceDetails.getCdoId());
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
	
	private AuditDTO getParentAuditForCommitId(BigDecimal commitId) {
		return commitWiseParentAudit.get(commitId);
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
	
	private void printDebugLine(String line) {
		System.out.println(line);
	}
	
	public List<PropertyAuditData> buildPropertyAudit() {
		
		List<PropertyAuditData> propertyAudit = new ArrayList<PropertyAuditData>();
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		queryCriteria.setPropertyName(this.propertyName);
		queryCriteria.setWithNewObjectChanges(true);
		Changes changes = auditService.getEntityChangesByCriteria(this.parentObject, queryCriteria);
		
		changes.forEach(change -> {
			if (change instanceof ValueChange) {
				ValueChange valueChange = (ValueChange) change;
				CommitMetadata commitData = valueChange.getCommitMetadata().get();
				propertyAudit.add(new PropertyAuditData(commitData.getAuthor(), 
						commitData.getCommitDate(), String.valueOf(valueChange.getRight())));
			}
		});
		
		return propertyAudit;
	}
	
}
