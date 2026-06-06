package com.akash.springboothello.service;

import com.akash.springboothello.dto.EmployeeRequest;
import com.akash.springboothello.exception.EmployeeNotFoundException;
import com.akash.springboothello.model.Employee;
import com.akash.springboothello.repository.EmployeeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees() {
       return repository.findAll();
    }


    public Employee addEmployee(Employee employee) {
        return  repository.save(employee);
    }

    @Cacheable(value = "employees", key = "#id")
    public Employee  getEmployeeById(Long id) {
        System.out.println("Fetching from DB");
        return repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));
    }

    @CacheEvict(value = "employees", key = "#id")
    public boolean deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
    @CachePut(value = "employees", key = "#id")
    public Employee updateEmployee(
            Long id,
            EmployeeRequest request) {

        Employee employee =
                repository.findById(id)
                        .orElse(null);

        if (employee == null) {
            return null;
        }

        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());

        return repository.save(employee);
    }
}