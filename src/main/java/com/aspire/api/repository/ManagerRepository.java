package com.aspire.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aspire.api.model.Department;
import com.aspire.api.model.Manager;
import java.util.Optional;


@Repository
public interface ManagerRepository extends MongoRepository<Manager,Integer>{
    Optional<Manager> findByDepartment(Department department);
}
