package com.enewschamp.audit.app;

import java.math.BigDecimal;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/enewschamp-api/v1/audit")
public class AuditController {

    private final Javers javers;

    @Autowired
    public AuditController(Javers javers) {
        this.javers = javers;
    }

    private String queryChanges(QueryBuilder jqlQuery) {
    	List<Change> changes = javers.findChanges(jqlQuery.build());
        return javers.getJsonConverter().toJson(changes);
    }
    
    @GetMapping("/entity/{entityName}")
    public String getAuditForEntity(@PathVariable String entityName) {
    	Class _class = null;
		try {
			_class = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
        QueryBuilder jqlQuery = QueryBuilder.byClass(_class).withChildValueObjects();
        
        return queryChanges(jqlQuery);
    }
    
    @GetMapping("/commits/{commitId}")
    public String getAuditByCommitId(@PathVariable BigDecimal commitId) {
        QueryBuilder jqlQuery = QueryBuilder.anyDomainObject().withCommitId(commitId);
        return queryChanges(jqlQuery);
    }
    
    
}