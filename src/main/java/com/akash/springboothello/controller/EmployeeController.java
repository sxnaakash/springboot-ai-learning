package com.akash.springboothello.controller;

import com.akash.springboothello.model.Employee;
import com.akash.springboothello.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import com.akash.springboothello.dto.EmployeeRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(
            @RequestBody EmployeeRequest request) {

        Employee employee = new Employee(
                employeeService.getEmployees().size() + 1,
                request.getName(),
                request.getDepartment());

        Employee savedEmployee =
                employeeService.addEmployee(employee);

        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable int id) {

        Employee employee =
                employeeService.getEmployeeById(id);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int id) {

        boolean deleted = employeeService.deleteEmployee(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int id,
            @RequestBody EmployeeRequest request) {

        Employee employee =
                employeeService.updateEmployee(id, request);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employee);
    }
}