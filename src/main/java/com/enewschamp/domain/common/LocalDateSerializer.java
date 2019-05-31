package com.enewschamp.domain.common;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    
    public LocalDateSerializer() {
        this(null);
    }
   
    public LocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }
 
    @Override
    public void serialize(
      LocalDate value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	jgen.writeString(value.toString());
    }
}