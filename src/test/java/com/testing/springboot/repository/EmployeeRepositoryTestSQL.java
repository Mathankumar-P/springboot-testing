package com.testing.springboot.repository;

import com.testing.springboot.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTestSQL {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;

    @BeforeEach
    public void setUp(){
        employee1 = Employee.builder().firstName("Mathankumar").lastName("P")
                .email("Temp@xyz.com").build();
        employeeRepository.save(employee1);
    }

    //Junit Test for Save Employee Operation
    @DisplayName("Junit Test for Save Employee Operation")
    @Test
    public void givenEmployeeObjectWhenSaveThenReturnEmployeeObject(){
        //given  - precondition
        // Employee employee1 = Employee.builder().firstName("Mathankumar").lastName("P")
        //                .email("Temp@xyz.com").build();
        //when - action or behaviour that is going to be tested
        Employee savedEmployee = employeeRepository.save(employee1);
        //then - verify the content
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit Test for Get all Employees
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){
        //given - precondition or setup
        Employee employee2 = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("Temp2@xyz.com").build();
        employeeRepository.save(employee2);
        //when - action or the  behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();
        //Then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }
     //Junit Test for findByID
     @Test
     public void givenEmployeeObject_whenFindById_thenReturnEmployee(){
        //given - precondition or setup
         //when - action or the  behaviour that we are going to test
         Employee fetch  = employeeRepository.findById(employee1.getId()).get();
         //Then - verify the output
         assertThat(fetch).isNotNull();
    }
    //Junit Test for Custom Query Method
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee(){
        //given - precondition or setup
        //when - action or the  behaviour that we are going to test
        Employee fetch  = employeeRepository.findByEmail(employee1.getEmail()).get();
        //Then - verify the output
        assertThat(fetch).isNotNull();
   }
    //Junit Test for Update function
   @Test
    public void givenEmployeeObject_whenUpdate_thenReturnEmployee(){
       //given - precondition or setup
       //when - action or the  behaviour that we are going to test
        Employee employee2 = employeeRepository.findById(employee1.getId()).get();
        employee2.setEmail("newemail@email.com");
       Employee updatedEmployee = employeeRepository.save(employee1);
       //Then - verify the output
       assertThat(updatedEmployee).isNotNull();
       assertThat(employee2.getEmail()).isEqualTo("newemail@email.com");
   }

    //Junit Test for Delete function
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition or setup
        //when - action or the  behaviour that we are going to test
        employeeRepository.delete(employee1);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());
        //Then - verify the output
        assertThat(employeeOptional).isEmpty();
    }
      //Junit Test for  Custom JPQL Query - index params
      @Test
      public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
           //given - precondition or setup
          // when - action or the  behaviour that we are going to test
          Employee employeeOptional = employeeRepository.findByJPQL(employee1.getFirstName(), employee1.getLastName());
          //Then - verify the output
          assertThat(employeeOptional).isNotNull();
    }
    //Junit Test for  Custom JPQL Query - named params
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        //when - action or the  behaviour that we are going to test
        Employee employeeOptional = employeeRepository.findByJPQLNamedParams(employee1.getFirstName(), employee1.getLastName());
        //Then - verify the output
        assertThat(employeeOptional).isNotNull();
    }
     //Junit Test for Native Sql Query index
     @Test
     public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
            //given - precondition or setup
            //when - action or the  behaviour that we are going to test
            Employee employeeOptional = employeeRepository.findByNativeSQL(employee1.getFirstName(), employee1.getLastName());
            //Then - verify the output
            assertThat(employeeOptional).isNotNull();
       }
       //Junit Test for Native Sql Query Named
     @Test
     public void givenFirstNameAndLastName_whenFindByNativeSQLNamed_thenReturnEmployeeObject(){
            //given - precondition or setup
            //when - action or the  behaviour that we are going to test
            Employee employeeOptional = employeeRepository.findByNativeSQLNamed(employee1.getFirstName(), employee1.getLastName());
            //Then - verify the output
            assertThat(employeeOptional).isNotNull();
       }




}