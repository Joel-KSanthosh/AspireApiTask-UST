package com.aspire.api.controller;

import java.util.List;

import com.aspire.api.model.Manager;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private String message;
    private List<Manager> details;

    public CustomResponse(String message){
        this.message = message;
    }

}
