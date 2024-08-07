package com.aspire.api.model;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Document(collection = "employees")
@Data
@Repository
public class Employee {
    @Id
    private Integer id;

    @NotBlank(message = "Name cannot be Empty")
    private String name;

    @NotNull(message = "Designation cannot be Empty")
    private Designation designation;

    @NotBlank(message = "Email cannot be Empty")
    private String email;

    @NotNull(message = "Department cannot be Empty")
    private Department department;

    @Positive(message = "Mobile number must be positive")
    private Long mobile;

    @NotBlank(message = "Location cannot be Empty")
    private String location;

    @PositiveOrZero(message = "Manager Id is not valid")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer managerId;

    @NotNull(message = "Date of Joining is Required")
    private ZonedDateTime dateOfJoining;

    private ZonedDateTime createdTime;

    private ZonedDateTime updatedTime;

    @Transient
    @JsonIgnore
    private boolean isPostOperation;


    @JsonIgnore
    public boolean isManager(){
        return getDesignation()==Designation.ACCOUNT_MANAGER && managerId==0;
    }

    @JsonIgnore
    public boolean isEmployee(){
        return getDesignation()==Designation.ASSOCIATE && managerId>0;
    }

}
