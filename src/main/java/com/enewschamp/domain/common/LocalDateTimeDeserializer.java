package com.enewschamp.domain.common;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> { 
 
    public LocalDateTimeDeserializer() { 
        this(null); 
    } 
 
    public LocalDateTimeDeserializer(Class<?> vc) { 
        super(vc); 
    }
 
    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return LocalDateTime.parse(node.textValue());
    }
} 