//package com.enewschamp.article.domain.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.javers.core.Changes;
//import org.javers.core.ChangesByCommit;
//import org.javers.core.ChangesByObject;
//import org.javers.core.Javers;
//import org.javers.core.commit.CommitMetadata;
//import org.javers.core.diff.changetype.ValueChange;
//import org.javers.core.diff.changetype.container.ContainerElementChange;
//import org.javers.core.diff.changetype.container.ElementValueChange;
//import org.javers.core.diff.changetype.container.ListChange;
//import org.javers.core.diff.changetype.container.ValueAdded;
//import org.javers.core.metamodel.object.CdoSnapshot;
//import org.javers.core.metamodel.object.CdoSnapshotState;
//import org.javers.core.metamodel.object.InstanceId;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import com.enewschamp.app.common.ErrorCodes;
//import com.enewschamp.app.dto.AuditDTO;
//import com.enewschamp.app.dto.FieldChangeDTO;
//import com.enewschamp.article.app.dto.NewsArticleAuditDTO;
//import com.enewschamp.article.app.dto.NewsArticleDTO;
//import com.enewschamp.article.app.dto.NewsArticleQuizAuditDTO;
//import com.enewschamp.article.app.dto.NewsArticleQuizDTO;
//import com.enewschamp.article.domain.entity.NewsArticle;
//import com.enewschamp.article.domain.entity.NewsArticleQuiz;
//import com.enewschamp.audit.domain.AuditService;
//import com.enewschamp.domain.common.OperationType;
//import com.enewschamp.problem.Fault;
//import com.enewschamp.problem.HttpStatusAdapter;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class NewsArticleServiceOld {
//
//	private final Javers javers;
//	
//	@Autowired
//	NewsArticleRepository repository;
//	
//	@Autowired
//	ModelMapper modelMapper;
//	
//	@Autowired
//	@Qualifier("modelPatcher")
//	ModelMapper modelMapperForPatch;
//	
//	@Autowired
//	AuditService auditService;
//	
//	@Autowired
//	ObjectMapper objectMapper;
//	
//	@Autowired
//    public NewsArticleServiceOld(Javers javers) {
//        this.javers = javers;
//    }
//	
//	public NewsArticle create(NewsArticle article) {
//		return repository.save(article);
//	}
//	
//	public NewsArticle update(NewsArticle article) {
//		Long articleId = article.getNewsArticleId();
//		NewsArticle existingEntity = load(articleId);
//		modelMapper.map(article, existingEntity);
//		return repository.save(existingEntity);
//	}
//	
//	public NewsArticle patch(NewsArticle newsArticle) {
//		Long articleId = newsArticle.getNewsArticleId();
//		NewsArticle existingEntity = load(articleId);
//		modelMapperForPatch.map(newsArticle, existingEntity);
//		return repository.save(existingEntity);
//	}
//	
//	public void delete(Long articleId) {
//		repository.deleteById(articleId);
//	}
//	
//	public NewsArticle load(Long articleId) {
//		Optional<NewsArticle> existingEntity = repository.findById(articleId);
//		if (existingEntity.isPresent()) {
//			return existingEntity.get();
//		} else {
//			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.ARTICLE_NOT_FOUND, "Article not found!");
//		}
//	}
//	
//	public NewsArticle get(Long articleId) {
//		Optional<NewsArticle> existingEntity = repository.findById(articleId);
//		if(existingEntity.isPresent()) {
//			return existingEntity.get();
//		}
//		return null;
//	}
//	
//	private Map<CommitMetadata, List<ChangesByCommit>> groupByCommitId(List<ChangesByCommit> allChangesByCommit) {
//		Map<CommitMetadata, List<ChangesByCommit>> commitIdWiseChanges = new HashMap<CommitMetadata, List<ChangesByCommit>>();
//		
//		for(ChangesByCommit changesByCommit: allChangesByCommit) {
//			CommitMetadata commitMetadata = changesByCommit.getCommit();
//			if(commitIdWiseChanges.get(commitMetadata) == null) {
//				commitIdWiseChanges.put(commitMetadata, new ArrayList<ChangesByCommit>());
//			}
//			commitIdWiseChanges.get(commitMetadata).add(changesByCommit);
//		}
//		return commitIdWiseChanges;
//	}
//	
//	public String getAudit(Long articleId) {
//		NewsArticle article = new NewsArticle();
//		article.setNewsArticleId(articleId);
//
//		List<ChangesByCommit> allChangesByCommit = new ArrayList<ChangesByCommit>();
//		
//		// Fetch article changes
//		Changes changes = auditService.getEntityChanges(article);
//		if(changes != null && changes.size() > 0) {
//			allChangesByCommit.addAll(changes.groupByCommit());
//		}
//		
//		// Fetch article quiz changes
//		article = load(articleId);
//		for(NewsArticleQuiz quiz: article.getNewsArticleQuiz()) {
//			Changes quizChanges = auditService.getEntityChanges(quiz);
//			if(quizChanges != null && quizChanges.size() > 0) {
//				allChangesByCommit.addAll(quizChanges.groupByCommit());
//			}
//		}
//		
//		Map<CommitMetadata, List<ChangesByCommit>> commitIdWiseChanges = groupByCommitId(allChangesByCommit);
//		
//		List<NewsArticleAuditDTO> articleAudits = new ArrayList<NewsArticleAuditDTO>();
//		
//		
//		//System.out.println("\n Printing Changes with grouping by commits and by objects :");
//		
//		commitIdWiseChanges.forEach((commitMetadata, changesByCommit) -> {
//			
//			println("  - Commit Id: " + commitMetadata.getId());
//			println("  - Operator: " + commitMetadata.getAuthor());
//			println("  - Operation Date Time: " + commitMetadata.getCommitDate());
//			
//			NewsArticleAuditDTO articleAuditDTO = new NewsArticleAuditDTO();
//			articleAuditDTO.setOperatorId(commitMetadata.getAuthor());
//			articleAuditDTO.setOperationDateTime(commitMetadata.getCommitDate());
//			articleAuditDTO.setCommitId(commitMetadata.getId().toString());
//			
//			articleAudits.add(articleAuditDTO);
//
//			changesByCommit.forEach(byCommit -> {
//				
//				byCommit.groupByObject().forEach(byObject -> {
//
//					String typeName = byObject.getGlobalId().getTypeName();
//					
//					
//					
//					println("\t  - Entity: " + byObject.getGlobalId().getTypeName());
//					println("\t  - Entity id: " + byObject.getGlobalId().value());
//					
//					CdoSnapshot snapshot = auditService.getEntitySnapshotByInstanceAndCommitId((InstanceId)byObject.getGlobalId(), commitMetadata.getId());
//					//System.out.println("Snapshot commit id " + snapshot.getCommitId());
//					//System.out.println("\t  - Operation type " + snapshot.getType());
//					//System.out.println("\t  - Snapshot: " + snapshot.getState());
//					
//					if(snapshot != null) {
//						if(typeName.equalsIgnoreCase(NewsArticle.class.getTypeName())) {
//							articleAuditDTO.setSnapshot(buildArticleSnapShotNode(snapshot));
//							articleAuditDTO.setAction(OperationType.fromSnapShotType(snapshot.getType()));
//						} else {
//							NewsArticleQuizAuditDTO quizAudit = new NewsArticleQuizAuditDTO();
//							quizAudit.setAction(OperationType.fromSnapShotType(snapshot.getType()));
//	    					quizAudit.setSnapshot(buildQuizSnapShotNode(snapshot));
//	            			articleAuditDTO.addChildObject(quizAudit);
//						}
//					}
//					
//			        byObject.get().forEach(change -> {
//			        	
//			            if(change instanceof ValueChange) {
//			            	ValueChange valueChange = (ValueChange) change;
//			            	println("\t\t  - Property Name: " + valueChange.getPropertyNameWithPath());
//			            	println("\t\t\t  - Change Type: " + change.getClass().getSimpleName());
//			            	println("\t\t\t  - Old Value: " + valueChange.getLeft());
//			            	println("\t\t\t  - New Value: " + valueChange.getRight());
//			            	System.out.println();
//			            	
//			            	FieldChangeDTO fieldChange = new FieldChangeDTO(valueChange.getPropertyNameWithPath(), valueChange.getLeft(), valueChange.getRight());
//			            	
//			            	if(typeName.equalsIgnoreCase(NewsArticle.class.getTypeName())) {
//			            		articleAuditDTO.addFieldChange(fieldChange);
//			            	} else {
//			            		
//			            	}
//			            	
//			            }
//			            
//			            if(change instanceof ListChange) {
//			            	ListChange listChange = (ListChange) change;
//			            	println("\t\t  - Property Name: " + listChange.getPropertyName());
//			            	List<ContainerElementChange> collectionChanges = listChange.getChanges();
//			            	
//			            	collectionChanges.forEach(collectionChange -> {
//			            		
//			            		if(collectionChange instanceof ValueAdded) {
//			            			System.out.println("\t\t\t - Collection Change Type: " + collectionChange.getClass().getSimpleName());
//			            			ValueAdded itemAdded = (ValueAdded) collectionChange;
//			            			InstanceId addedItemInstanceDetails = (InstanceId) itemAdded.getValue();
//			            			println("\t\t\t  - Item Type: " + addedItemInstanceDetails.getTypeName());
//			            			println("\t\t\t  - Item Id: " + addedItemInstanceDetails.getCdoId());
//			            			
//			            			NewsArticleQuizAuditDTO quizAudit = new NewsArticleQuizAuditDTO();
//			            			quizAudit.setAction(OperationType.Add);
//
//			    					CdoSnapshot quizSnapShot = auditService.getEntitySnapshotByInstanceAndCommitId(addedItemInstanceDetails, commitMetadata.getId());
//			    					quizAudit.setSnapshot(buildQuizSnapShotNode(quizSnapShot));
//			            			articleAuditDTO.addChildObject(quizAudit);
//
//			            		}
//			            		
//			            		if(collectionChange instanceof ElementValueChange) {
//			            			ElementValueChange itemExchanged = (ElementValueChange) collectionChange;
//			            			
//			            			// In case of element change, left value is the removed item
//			            			InstanceId removedItemInstanceDetails = (InstanceId) itemExchanged.getLeftValue();
//			            			println("\t\t\t - Collection Change Type: " + "ValueRemoved");
//			            			println("\t\t\t  - Item Type: " + removedItemInstanceDetails.getTypeName());
//			            			println("\t\t\t  - Item Id: " + removedItemInstanceDetails.getCdoId());
//			            			
//			            			NewsArticleQuizAuditDTO quizAudit = new NewsArticleQuizAuditDTO();
//			            			quizAudit.setAction(OperationType.Remove);
//			    					CdoSnapshot quizSnapShot = auditService.getEntitySnapshotByInstanceAndCommitId(removedItemInstanceDetails, commitMetadata.getId());
//			    					quizAudit.setSnapshot(buildQuizSnapShotNode(quizSnapShot));
//			            			articleAuditDTO.addChildObject(quizAudit);
//			            			
//			            			// In case of element change, left value is the removed item
//			            			InstanceId addedItemInstanceDetails = (InstanceId) itemExchanged.getRightValue();
//			            			println("\t\t\t - Collection Change Type: " + "ValueAdded");
//			            			println("\t\t\t  - Item Type: " + addedItemInstanceDetails.getTypeName());
//			            			println("\t\t\t  - Item Id: " + addedItemInstanceDetails.getCdoId());
//			            			
//			            			quizAudit = new NewsArticleQuizAuditDTO();
//			            			quizAudit.setAction(OperationType.Add);
//			            			quizSnapShot = auditService.getEntitySnapshotByInstanceAndCommitId(addedItemInstanceDetails, commitMetadata.getId());
//			    					quizAudit.setSnapshot(buildQuizSnapShotNode(quizSnapShot));
//			            			articleAuditDTO.addChildObject(quizAudit);
//			            		}
//			            		
//			            	});
//			            	System.out.println();
//			            }
//			            
//			            
//			            //System.out.println("  - " + change );
//			            
//			        });
//			    });
//			});
//			
//		});
//		
//		
//		System.out.println(articleAudits.toString());
//		
//		String response = null;
//		
//		try {
//			response = objectMapper.writeValueAsString(articleAudits);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//return javers.getJsonConverter().toJson(articleAudits);
//		
//		System.out.println("Audit: \n" + auditService.getEntityAudit(article));
//		return response;
//		//return auditService.getEntityAudit(article);
//	}
//	
//	private JsonNode buildQuizSnapShotNode(CdoSnapshot snapshot) {
//		
//		NewsArticleQuizDTO newsArticleQuizDTO = null;
//		JsonNode node = null;
//		 
//		if(snapshot != null) {
//
//			CdoSnapshotState state = snapshot.getState();
//			
//			Map<String , Object> valueMap = new HashMap<String, Object>();
//			for(String propertyName: state.getPropertyNames()) {
//				valueMap.put(propertyName, state.getPropertyValue(propertyName));
//			}
//			
//			newsArticleQuizDTO = objectMapper.convertValue(valueMap, NewsArticleQuizDTO.class);
//			//System.out.println("\t  - Snapshot: " + newsArticleQuizDTO);
//			
//			node = objectMapper.convertValue(valueMap, JsonNode.class);
//		}
//		
//		//return newsArticleQuizDTO;
//		return node;
//	}
//	
//	private JsonNode buildArticleSnapShotNode(CdoSnapshot snapshot) {
//		
//		NewsArticleDTO newsArticleDTO = null;
//		JsonNode node = null;
//		 
//		if(snapshot != null) {
//
//			CdoSnapshotState state = snapshot.getState();
//			
//			Map<String , Object> valueMap = new HashMap<String, Object>();
//			for(String propertyName: state.getPropertyNames()) {
//				valueMap.put(propertyName, state.getPropertyValue(propertyName));
//			}
//			
//			valueMap.remove("newsArticleQuiz");
//			
//			newsArticleDTO = objectMapper.convertValue(valueMap, NewsArticleDTO.class);
//			System.out.println("\t  - Article Snapshot: " + newsArticleDTO);
//			
//			node = objectMapper.convertValue(valueMap, JsonNode.class);
//			System.out.println("\t  - Article Snapshot with Jsonnode: " +  node);
//		}
//		
//		//return newsArticleDTO;
//		return node;
//	}
//	
//	private void println(String printString) {
//		System.out.println(printString);
//	}
//	
//	
//}
