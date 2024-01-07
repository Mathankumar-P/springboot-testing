package com.testing.springboot.controller;

import com.testing.springboot.entity.Employee;
import com.testing.springboot.service.EmployeeService;
import com.testing.springboot.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private EmployeeService employeeService ;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        return  employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    @PutMapping ("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable ("id") long employeeId, @RequestBody Employee employee){
        return  employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                    savedEmployee.setFirstName( employee.getFirstName());
                    savedEmployee.setLastName( employee.getLastName());
                    savedEmployee.setEmail( employee.getEmail());
                   Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                   return new ResponseEntity<>(updatedEmployee,HttpStatus.OK);
                }) . orElseGet( ()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Employee Deleteted Successfuly ", HttpStatus.OK);
    }

}
