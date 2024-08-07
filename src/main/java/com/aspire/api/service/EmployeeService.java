package com.aspire.api.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aspire.api.model.Employee;
// import com.aspire.api.model.Manager;
import com.aspire.api.repository.EmployeeRepository;
// import com.aspire.api.repository.ManagerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;

    // @Autowired
    // private ManagerRepository managerRepository;

    //Creating new employees
    public void createEmployee(Employee employee){
        employee.setCreatedTime(ZonedDateTime.now());
        employee.setUpdatedTime(ZonedDateTime.now());
        employeeRepository.save(employee);
        
    }

    //Finding the id for inserting into db
    public Integer findNextId(){
        Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.DESC,"_id"))
        .stream().findFirst().orElse(null);

        if(employee==null){
            return 1;
        }
        else{
            return employee.getId()+1;
        }
    }

    //Find the employee by id
    public Employee findEmployeeById(Integer id){
        return employeeRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Employee with given id doesn't exist")
            );
    }

    //Find delete employee by id
    public void deleteEmployeeById(Integer id){
        employeeRepository.deleteById(id);
    }

    //Checks if duplicate number exists
    public void checkDuplicatePhoneNumber(Long number){
        Optional<Employee> employee = employeeRepository.findByMobile(number);
        if(employee.isPresent()){
            throw new KeyAlreadyExistsException("Entered Mobile number already exists.");
        }
    }

     //Checks if duplicate email exists
    public void checkDuplicateEmail(String email){
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if(employee.isPresent()){
            throw new KeyAlreadyExistsException("Entered Email already exists.");
        }
    }

    //Change the employees current manager
    public void changeEmployeeManager(Employee employee,Integer managerId){
        employee.setManagerId(managerId);
        employee.setUpdatedTime(ZonedDateTime.now());

        //If employee is under same department then change the current department of employee
        // Manager manager = managerRepository.findById(managerId).orElseThrow(
        //     () -> new IllegalArgumentException("Manager with given id doesn't exist!")
        // );
        // employee.setDepartment(manager.getDepartment());

        employeeRepository.save(employee);
    }
    
}
