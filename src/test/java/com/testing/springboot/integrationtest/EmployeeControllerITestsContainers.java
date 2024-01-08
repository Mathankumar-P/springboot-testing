package com.testing.springboot.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.springboot.entity.Employee;
import com.testing.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class EmployeeControllerITestsContainers extends AbstractBaseTest{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
        employee = Employee.builder().id(1L).firstName("Mathankumar").lastName("P")
                .email("testing@123.com").build();
    }
    @Test
    public void givenEmployeeObject_whenCreate_thenReturnSavedEmployeeObject() throws Exception {
        //given - precondition or setup
        //when - action or the  behaviour that we are going to test
        // Mock the Rest request using MockMvc method and catch the response
        ResultActions response = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //Then - verify the output of response
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));
    }
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception{
        //given - precondition or setup (stubbing)
        Employee employee1 = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("temp@mail.com").build();
        List<Employee> employeeList = new ArrayList<>( List.of(employee1,employee));
        employeeRepository.saveAll(employeeList);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print()).
                andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        employeeRepository.save(employee);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}",employee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                .andExpect((jsonPath("$.lastName",is(employee.getLastName()))))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));
    }

    //Junit Test for Get Employee By Id - Negative Scenario
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        employeeRepository.save(employee);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}",employeeId));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        employeeRepository.save(employee);
        Employee updatedEmployee = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("new1test@123.com").build();

        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}",employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(updatedEmployee.getFirstName())))
                .andExpect((jsonPath("$.lastName",is(updatedEmployee.getLastName()))))
                .andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())));;
    }

    //Junit Test for update Employee - Negative Scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        Employee updatedEmployee = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("new1test@123.com").build();
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit Test for Delete Employee
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print());
    }
}
