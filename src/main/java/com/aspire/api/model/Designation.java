package com.aspire.api.model;

import com.aspire.api.service.DesignationDeserialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = DesignationDeserialization.class)
public enum Designation {
    ACCOUNT_MANAGER("Account Manager"),
    ASSOCIATE("Associate");

    private String value;

    Designation(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

    public boolean contains(String check){
        for(Designation designation : Designation.values()){
            if(designation.value.equalsIgnoreCase(check)){
                return true;
            }
        }
        throw new IllegalArgumentException("Unknown designation: " + check);
    }
}
