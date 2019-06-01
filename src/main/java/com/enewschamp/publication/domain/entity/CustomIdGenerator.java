package com.enewschamp.publication.domain.entity;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.boot.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class CustomIdGenerator extends IdentityGenerator implements Configurable {

    private IdentifierGenerator defaultGenerator;

    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        Long idValue = (Long)defaultGenerator.generate(session, object);
        //idValue will be assigned your entity id
        return idValue;
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        DefaultIdentifierGeneratorFactory dd = new DefaultIdentifierGeneratorFactory();
        //dd.setDialect(d);
        defaultGenerator = dd.createIdentifierGenerator("sequence", type, params);
    }
}