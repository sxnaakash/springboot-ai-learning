package com.akash.springboothello.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Department is required")
    private String department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}