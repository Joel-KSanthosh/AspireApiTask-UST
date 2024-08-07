package com.aspire.api.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.aspire.api.model.Employee;
import com.aspire.api.model.Manager;

@Component
public class Sorter {

    public List<Manager> sortManager(List<Manager> managers){

        List<Manager> sortedManagers = managers.stream()
            .sorted(Comparator.comparing(Manager::getId)).collect(Collectors.toList());

        for(Manager currManager : sortedManagers){
            List<Employee> sortedEmployees = currManager.getEmployeeList().stream()
                .sorted(Comparator.comparing(Employee::getId))
                    .collect(Collectors.toList());
            currManager.setEmployeeList(sortedEmployees);
        }
        
        return sortedManagers;
    }
      
}
