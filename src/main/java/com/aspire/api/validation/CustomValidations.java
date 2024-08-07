package com.aspire.api.validation;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.stereotype.Service;

import com.aspire.api.model.Department;
import com.aspire.api.model.Designation;
import com.aspire.api.model.Employee;
import com.aspire.api.service.EmployeeService;

@Service
public class CustomValidations {
    private static final String EMAIL_REGEX = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    public void validateDate(Employee employee){
        if(employee.getDateOfJoining().isAfter(employee.getCreatedTime())){
            throw new ValidationException("Enter a valid Date of Joining");
        }
    }

    public void validateDesignation(Employee employee){
        boolean isValidDesignation = Arrays.stream(Designation.values()).anyMatch(d -> d == employee.getDesignation());
        
        if(!isValidDesignation){
            throw new ValidationException("Enter a valid Designation");
        }
    }

    public void validateDepartment(Employee employee){
        boolean isValidDepartment = Arrays.stream(Department.values()).anyMatch(d -> d == employee.getDepartment());

        if(!isValidDepartment){
            throw new ValidationException("Enter a valid Department");
        }
    }

    public void validateMobile(Employee employee,EmployeeService employeeService ){
        int len = String.valueOf(employee.getMobile()).length();
        if(len!=10){
            throw new ValidationException("Mobile number is not valid");
        }

        Long number = employee.getMobile();
        String numberString = Long.toString(number);
        char ch = numberString.charAt(0);
        int firstDigit = Character.getNumericValue(ch);
        if (firstDigit < 6) {
            throw new ValidationException("Mobile number is not valid");
        }
        employeeService.checkDuplicatePhoneNumber(number);
    }


    public void validateManager(Employee employee){
        if(employee.getManagerId()==0 && employee.getDesignation().equals(Designation.ASSOCIATE)){
            throw new ValidationException("Enter a valid Manager Id");
        }
    }

    public void validateLocation(Employee employee){
        if(employee.getLocation().length()<=1){
            throw new ValidationException("Enter a valid Location");
        }
    }

    public void validateEmail(Employee employee,EmployeeService employeeService){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(employee.getEmail());

        if(!matcher.matches()){
            throw new ValidationException("Enter a valid Email");
        }

        employeeService.checkDuplicateEmail(employee.getEmail());
    }

}
