package com.aspire.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aspire.api.model.Employee;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,Integer>{
    Optional<Employee> findByMobile(Long mobile);  
    Optional<Employee> findByEmail(String email); 
}
