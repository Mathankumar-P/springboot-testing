package com.testing.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.springboot.entity.Employee;
import com.testing.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static  org.hamcrest.CoreMatchers.*;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
     //Junit Test for create Employee

    @BeforeEach
    void setUp(){
        employee = Employee.builder().id(1L).firstName("Mathankumar").lastName("P")
                .email("testing@123.com").build();
    }
     @Test
     public void givenEmployeeObject_whenCreate_thenReturnSavedEmployeeObject() throws Exception {
         //given - precondition or setup
         given(employeeService.saveEmployee(any(Employee.class))).
                 willAnswer((invocation)-> invocation.getArgument(0));
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
 //Junit Test for get All Employees
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception{
        //given - precondition or setup (stubbing)
        Employee employee1 = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("temp@mail.com").build();
        List<Employee> employeeList = new ArrayList<>( List.of(employee1,employee));
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees"));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print()).
                andExpect(jsonPath("$.size()", is(employeeList.size())));
   }
 //Junit Test for Get Employee By Id - Positive Scenario
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Long employeeId = 1L;
        given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}",employeeId));
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
        given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}",employeeId));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
 //Junit Test for update Employee - Positive Scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("new1test@123.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class))).
                willAnswer((invocation)-> invocation.getArgument(0));
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}",employeeId)
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
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().firstName("Siranjeevi").lastName("M")
                .email("new1test@123.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).
                willAnswer((invocation)-> invocation.getArgument(0));
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/v1/employees/{id}",employeeId)
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
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //when - action or the  behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/v1/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isOk()).andDo(print());
   }




}