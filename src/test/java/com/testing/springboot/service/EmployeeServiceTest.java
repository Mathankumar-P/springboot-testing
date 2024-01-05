package com.testing.springboot.service;

import com.testing.springboot.entity.Employee;
import com.testing.springboot.exception.ResourceNotFoundException;
import com.testing.springboot.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;
    @BeforeEach
    public void setUP(){
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService=new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder().firstName("Mathankumar").lastName("P")
                .email("testsign@xml.com").id(1L).build();
    }

     //Junit Test for SaveEmployee method
     @Test
     public void givenEmployeeObject_whenSaveEmployeeMethod_thenReturnEmployee(){
            //given - precondition or setup
            given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
            given(employeeRepository.save(employee)).willReturn(employee);
            //when - action or the  behaviour that we are going to test
            Employee savedEmployee = employeeService.saveEmployee(employee);
            //Then - verify the output
            assertThat(savedEmployee).isNotNull();
    }
    //Junit Test for SaveEmployee method
    @Test
    public void givenExistingEmail_whenSaveEmployeeMethod_thenThrowsException(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //when - action or the  behaviour that we are going to test
        //Since the mail id exist is throws a custom exception error we need to handle. to handle user Assertion.assertThrows
        assertThrows(ResourceNotFoundException.class,()-> {
            employeeService.saveEmployee(employee);
        });
        //then
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    //Junit Test for get All employee - Positive Response
    @Test
    public void givenEmployeeList_whenGetAllEmployeeList_thenReturnEmployeeList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("testsign@zoom.com").id(1L).build();
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
        //when - action or the  behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //Then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }
    //Junit Test for get All employee - Negative Scenario Response
    @Test
    public void givenEmptyListOfEmployee_whenGetAllEmployeeList_thenReturnEmptyEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when - action or the  behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //Then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

 //Junit Test for Get Employee By ID
    @Test
    public void givenEmployeeId_whenGetEmployeeByID_thenReturnEmployee(){
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when - action or the  behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
        //Then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    //Junit Test for Update Employee By ID
    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnEmployee(){
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("new@gmail.com");
        employee.setFirstName("Rajan");
        //when - action or the  behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        //Then - verify the output
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("new@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Rajan");
    }

    //Junit Test for Delete Employee
    @Test
    public void givenEmployeeID_whenDeleteEmployee_thenRemoveEmployee(){
        //given - precondition or setup
        Long employeeId =1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        //when - action or the  behaviour that we are going to test
         employeeService.deleteEmployee(employeeId);
        //Then - verify the output
        verify(employeeRepository,times(1)).deleteById(employeeId);
    }
}
