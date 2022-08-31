package com.karthik.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karthik.springboot.model.Employee;
import com.karthik.springboot.service.EmployeeService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        Employee employee = Employee.builder()
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

       
        ResultActions response = mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }

    // unit test case to get all employees
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Karthik").lastName("Pavan").email("karthik@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("yogish").lastName("hp").email("hp@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));

    }

    // valid employee id
    // unit test case to get employee by id
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    // negative scenario with no employee id
    // unit test case to get employee by id
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // unit test to update employee 
        @Test
        public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
            long employeeId = 1L;
            Employee savedEmployee = Employee.builder()
                    .firstName("Karthik")
                    .lastName("Pavan")
                    .email("karthik@gmail.com")
                    .build();

            Employee updatedEmployee = Employee.builder()
                    .firstName("pavan")
                    .lastName("hy")
                    .email("hy@gmail.com")
                    .build();
            given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
            given(employeeService.updateEmployee(any(Employee.class)))
                    .willAnswer((invocation)-> invocation.getArgument(0));

            ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedEmployee)));


            // verify the output
            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                    .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
        }

    // unit test to update employee REST API - negative way
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("raja")
                .lastName("hp")
                .email("raja@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

// unit test to delete employee 
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
