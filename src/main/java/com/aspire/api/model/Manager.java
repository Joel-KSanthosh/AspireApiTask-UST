package com.aspire.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "managers")
@Data
@AllArgsConstructor
public class Manager {
    @Id
    private Integer id;
    private String accountManager;
    private Department department;
    private List<Employee> employeeList;

}
