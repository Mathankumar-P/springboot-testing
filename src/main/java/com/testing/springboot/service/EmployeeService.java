package com.testing.springboot.service;

import com.testing.springboot.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);
     Employee updateEmployee(Employee employee);

     void deleteEmployee(Long id);
}
