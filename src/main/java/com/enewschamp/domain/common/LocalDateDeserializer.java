package com.enewschamp.domain.common;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> { 
 
    public LocalDateDeserializer() { 
        this(null); 
    } 
 
    public LocalDateDeserializer(Class<?> vc) { 
        super(vc); 
    }
 
    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	
        JsonNode node = jp.getCodec().readTree(jp);
//        
//        int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        String itemName = node.get("itemName").asText();
//        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
        
        
 
        return LocalDate.parse("2018-11-12");
    }
}