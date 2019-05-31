package com.enewschamp.audit.app;

import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.article.domain.entity.NewsArticle;

@RestController
@RequestMapping(value = "/enewschamp-api/v1/audit")
public class AuditController {

    private final Javers javers;

    @Autowired
    public AuditController(Javers javers) {
        this.javers = javers;
    }

    @GetMapping("/article")
    public String getArticleChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(NewsArticle.class).withChildValueObjects();
        return queryChanges(jqlQuery);
    }
    
    @RequestMapping("/article/{articleId}")
    public String getArticleChanges(@PathVariable Long articleId) {
    	NewsArticle article = new NewsArticle();
    	article.setNewsArticleId(articleId);
    	
        QueryBuilder jqlQuery = QueryBuilder.byInstance(article).withChildValueObjects();
        return queryChanges(jqlQuery);
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
}