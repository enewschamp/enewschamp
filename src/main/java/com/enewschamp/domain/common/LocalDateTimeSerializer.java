package com.enewschamp.domain.common;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    
    public LocalDateTimeSerializer() {
        this(null);
    }
   
    public LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }
 
    @Override
    public void serialize(
    		LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	jgen.writeString(value.toString());
        //jgen.writeStartObject();
        //jgen.writeStringField("dateString", value.toString());
        //jgen.writeEndObject();
    }
}