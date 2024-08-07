package com.aspire.api.controller;

import com.aspire.api.model.Employee;
import com.aspire.api.model.Manager;
import com.aspire.api.service.EmployeeService;
import com.aspire.api.service.ManagerService;
import com.aspire.api.utils.Sorter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Sorter sorter;

    @Transactional
    @PostMapping("/employee/add")
    public ResponseEntity<CustomResponse> addEmployees(@Valid @RequestBody Employee employee) {
        employee.setPostOperation(true);
        employee.setId(employeeService.findNextId());
        if(employee.isManager()){
            managerService.checkIfDepartmentManagerExists(employee.getDepartment());
            Manager manager = new Manager(
                employeeService.findNextId(),
                employee.getName(),
                employee.getDepartment(),
                new ArrayList<>()
            );
            employeeService.createEmployee(employee);
            managerService.createManager(manager);
            return new ResponseEntity<>(
                new CustomResponse("Successfully created."),
                HttpStatus.CREATED
            );
        }
        else if(employee.isEmployee()){
            managerService.updateCreateManagerEmployeeList(employee,employee.getManagerId());
            return new ResponseEntity<>(
                new CustomResponse("Successfully created."),
                HttpStatus.CREATED
            );
        }
        else{
            return new ResponseEntity<>(
                new CustomResponse("Invalid input Employee or Manager"),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/employee")
    public ResponseEntity<CustomResponse> getEmployeeDetails(
        @RequestParam(required = false) Integer managerId,
        @RequestParam(name = "year-of-experience",required = false) Integer experience
    ){
        if(managerId!=null && experience!=null){
            if(managerId<0){
                return new ResponseEntity<>(
                    new CustomResponse("Invalid managerId"),
                    HttpStatus.BAD_REQUEST
                );
            }
            else if(experience<0){
                return new ResponseEntity<>(
                    new CustomResponse("Invalid input for experience"),
                    HttpStatus.BAD_REQUEST
                );
            }
            else{
                Manager manager = managerService.getManagerById(managerId);
                List<Manager> managers = managerService.filterByExperience(manager, experience);

                return new ResponseEntity<>(
                    new CustomResponse("succesfuly fetched", sorter.sortManager(managers)),
                    HttpStatus.OK
                    );
            }

        }
        else if(managerId!=null){
            if(managerId<0){
                return new ResponseEntity<>(
                    new CustomResponse("Invalid managerId"),
                    HttpStatus.BAD_REQUEST
                );
            }

            List<Manager> managers = Arrays.asList(managerService.getManagerById(managerId));
            
            return new ResponseEntity<>(
                new CustomResponse("succesfuly fetched",sorter.sortManager(managers)),
                HttpStatus.OK
                );
        }
        else if(experience!=null){
            if(experience<0){
                return new ResponseEntity<>(new CustomResponse("Invalid input for experience"),
                HttpStatus.BAD_REQUEST);
            }
            List<Manager> allManagers = managerService.getAllManagers();
            List<Manager> managers = managerService.filterByExperience(allManagers, experience);

            return new ResponseEntity<>(
                new CustomResponse("succesfuly fetched",sorter.sortManager(managers)),
                HttpStatus.OK
                );
        }
        else{
            List<Manager> managers = managerService.getAllManagers();
            
            return new ResponseEntity<>(new CustomResponse("succesfuly fetched",sorter.sortManager(managers)),
            HttpStatus.OK
            );
        }
    }

    @DeleteMapping("/employee/delete")
    public ResponseEntity<CustomResponse> deleteEmployee(@RequestParam Integer employeeId){

        Employee employee = employeeService.findEmployeeById(employeeId);
        String empName = employee.getName();
        if(employee.isManager()){
            Manager manager = managerService.getManagerById(employeeId);
            if(manager.getEmployeeList().size()==0){
                employeeService.deleteEmployeeById(employeeId);
                managerService.deleteManagerById(employeeId);
                return new ResponseEntity<>(new CustomResponse("Successfully deleted "+empName+" from employee list of the organization"),
                HttpStatus.OK
                );
            }
            else{
                return new ResponseEntity<>(new CustomResponse("Unable to delete employee "+empName),
                HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        else{
            employeeService.deleteEmployeeById(employeeId);
            managerService.deleteEmployeeFromManager(employee, employee.getManagerId());
            return new ResponseEntity<>(new CustomResponse("Successfully deleted "+empName+" from employee list of the organization"),
            HttpStatus.OK
            );
        }

    }

    @PutMapping("/employee/update")
    @Transactional
    public ResponseEntity<CustomResponse> updateEmployeeManager(
        @RequestParam Integer employeeId,
        @RequestParam Integer managerId
        ) {
        
        Employee employee = employeeService.findEmployeeById(employeeId);

        String empName = employee.getName();
        
        if(employee.getManagerId()== managerService.getManagerById(managerId).getId()){
            throw new ValidationException("Employee is already working under the same manager");
        }

        String prevManagerName = employeeService.findEmployeeById(employee.getManagerId()).getName();
        String nextManagerName = employeeService.findEmployeeById(managerId).getName();

        managerService.deleteEmployeeFromManager(employee, employee.getManagerId());
        employeeService.changeEmployeeManager(employee, managerId);
        managerService.updateManagerEmployeeList(employee, managerId);

        return new ResponseEntity<CustomResponse>(
            new CustomResponse(empName+"'s manager has been successfully changed from "+prevManagerName+" to "+nextManagerName+"."),
            HttpStatus.OK);
    }
    
    
}
