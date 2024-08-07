package com.aspire.api.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.aspire.api.model.Employee;
import com.aspire.api.service.EmployeeService;
import com.aspire.api.validation.CustomValidations;

@Component(value = "validations")
public class EmployeeValidationEvent extends AbstractMongoEventListener<Employee>{  
    @Autowired
    EmployeeService employeeService;

    @Autowired
    private CustomValidations employeeValidations; 

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Employee> event){
        Employee employee = event.getSource();
        
        if(employee.isPostOperation()){
            employeeValidations.validateDate(employee);
            employeeValidations.validateEmail(employee, employeeService);
            employeeValidations.validateDepartment(employee);
            employeeValidations.validateDesignation(employee);
            employeeValidations.validateMobile(employee,employeeService);
            employeeValidations.validateManager(employee);
            employeeValidations.validateLocation(employee);
        }
        
    }

}
