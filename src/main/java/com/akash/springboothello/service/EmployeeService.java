package com.akash.springboothello.service;

import com.akash.springboothello.dto.EmployeeRequest;
import com.akash.springboothello.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        employees.add(new Employee(1, "Akash", "Java"));
        employees.add(new Employee(2, "Rahul", "DevOps"));
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    public Employee addEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    public Employee getEmployeeById(int id) {

        return employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean deleteEmployee(int id) {
        return employees.removeIf(emp -> emp.getId() == id);
    }

    public Employee updateEmployee(int id, EmployeeRequest request) {

        Employee employee = getEmployeeById(id);

        if (employee == null) {
            return null;
        }

        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());

        return employee;
    }
}