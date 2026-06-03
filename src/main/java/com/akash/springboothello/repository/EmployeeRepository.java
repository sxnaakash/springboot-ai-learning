package com.akash.springboothello.repository;

import com.akash.springboothello.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {
}