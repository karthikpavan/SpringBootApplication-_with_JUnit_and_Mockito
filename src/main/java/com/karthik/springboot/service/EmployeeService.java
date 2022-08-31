package com.karthik.springboot.service;

import com.karthik.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
	
    List<Employee> getAllEmployees();
	
    Optional<Employee> getEmployeeById(long id);
	
    Employee updateEmployee(Employee updatedEmployee);
	
    void deleteEmployee(long id);
}
