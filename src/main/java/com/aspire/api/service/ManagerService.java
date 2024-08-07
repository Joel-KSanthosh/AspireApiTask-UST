package com.aspire.api.service;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.api.model.Department;
import com.aspire.api.model.Employee;
import com.aspire.api.model.Manager;
import com.aspire.api.repository.ManagerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeService employeeService;

    //For finding manager by id
    public Manager getManagerById(Integer id){
        return managerRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Manager with given id does not exist")
        );
    }
    
    //Creating manager
    public void createManager(Manager manager){
        managerRepository.save(manager);
    }

    /*
        For creating an employee under a manager
     */
    public void updateCreateManagerEmployeeList(Employee employee ,Integer id){
        Manager existingManager = managerRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Manager with given id does not exist")
        );

        //If employees under a manger must work in a same department
        // if(employee.getDepartment()!=existingManager.getDepartment()){
        //     throw new IllegalArgumentException("Entered manager works in a different department ----> "
        //         +existingManager.getDepartment());
        // }
        
        employeeService.createEmployee(employee);
        existingManager.getEmployeeList().add(employee);
        managerRepository.save(existingManager);
        

    }

    /*
        For adding an existing employee under an existing manager
     */
    public void updateManagerEmployeeList(Employee employee ,Integer id){
        Manager existingManager = managerRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Manager with given id does not exist")
        );
        if (!existingManager.getEmployeeList().contains(employee)) {
            existingManager.getEmployeeList().add(employee);
            managerRepository.save(existingManager);
        } else {
            throw new IllegalArgumentException("Employee already exists in manager's list");
        }
        
    }

    //Listing all managers
    public List<Manager> getAllManagers(){
        return managerRepository.findAll();
    }

    //Filtering managers by Experience
    public List<Manager> filterByExperience(List<Manager> managers,Integer experience){

        /*
         For finding the managers who has the given experience
         */
        List<Manager> experiencedManagers = new ArrayList<>();
        for(Manager manager : managers){
            if(calculateYearsofExperience(employeeService
                .findEmployeeById(manager.getId())
                    .getDateOfJoining())>=experience){
                        experiencedManagers.add(manager);
                    }
        }

        /*
         For finding employees who has given experience under
         even if the manager has the required experience or not
         */
        // return managers.stream()
        //     .peek(manager -> manager.setEmployeeList(manager.getEmployeeList().stream()
        //         .filter(employee -> calculateYearsofExperience(employee.getDateOfJoining())>=experience)
        //         .collect(Collectors.toList())
        //     )).collect(Collectors.toList());


        /*
        For finding employees who has given experience
         under manager who has the required experience
         */
        return experiencedManagers.stream()
            .peek(manager -> manager.setEmployeeList(manager.getEmployeeList().stream()
                .filter(employee -> calculateYearsofExperience(employee.getDateOfJoining())>=experience)
                .collect(Collectors.toList())
            )).collect(Collectors.toList());
    }

    //Filtering given manager by experience
    public List<Manager> filterByExperience(Manager manager,Integer experience){
        if(calculateYearsofExperience(employeeService.
            findEmployeeById(manager.getId()).getDateOfJoining())>=experience){
                manager.setEmployeeList(
                    manager.getEmployeeList().stream()
                        .filter(employee -> calculateYearsofExperience(employee.getDateOfJoining())>=experience)
                            .collect(Collectors.toList()));

        return Arrays.asList(manager);

        }
        else{
            throw new IllegalArgumentException("Manager with given years of experience doesn't exist");
            // return new ArrayList<>();

        }
        
    }


    //Calculating the years of experience
    public int calculateYearsofExperience(ZonedDateTime date){
        return Period.between(date.toLocalDate(),  ZonedDateTime.now().toLocalDate()).getYears();
    }

    //Deleting manager with given id
    public void deleteManagerById(Integer id){
        managerRepository.deleteById(id);
    }

    //Removing an employee from the manager
    public void deleteEmployeeFromManager(Employee employee,Integer id){
        Manager manager = managerRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Error db failed")
            );
            if (manager.getEmployeeList().contains(employee)) {
                manager.getEmployeeList().remove(employee);
                managerRepository.save(manager);
            } 
            else {
                throw new IllegalArgumentException("Employee not found in manager's list");
            }
    }

    //For checking if manager for a particular department exists
    public void checkIfDepartmentManagerExists(Department department){
        Optional<Manager> manager = managerRepository.findByDepartment(department);
        if(manager.isPresent()){
            throw new ValidationException("Manager for "+department+" department already exists.");
        }
    }

}
