package com.aspire.api.service;

import java.io.IOException;

import com.aspire.api.model.Designation;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DesignationDeserialization extends JsonDeserializer<Designation>{

    @Override
    public Designation deserialize(JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        String value = p.getText();
        for(Designation designation : Designation.values()){
            if(designation.toString().equalsIgnoreCase(value)){
                return designation;
            }
        }
        throw new UnsupportedOperationException("Unknown designation: " + value);
    }
    
}
