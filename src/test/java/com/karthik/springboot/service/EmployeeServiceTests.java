package com.karthik.springboot.service;

import com.karthik.springboot.exception.ResourceNotFoundException;
import com.karthik.springboot.model.Employee;
import com.karthik.springboot.repository.EmployeeRepository;
import com.karthik.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail.com")
                .build();
    }

    // JUnit test to saveEmployee method
    @DisplayName("JUnit test to saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given  
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when  
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);
        // then 
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test to saveEmployee method
    @DisplayName("JUnit test to saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given  
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when  
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test to getAllEmployees method
    @DisplayName("JUnit test to getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        // given  

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Raja")
                .lastName("HY")
                .email("raja@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        // when  
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then 
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test to getAllEmployees method
    @DisplayName("JUnit test to getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        // given  

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Raja")
                .lastName("HY")
                .email("raja@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when  
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then 
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test to getEmployeeById method
    @DisplayName("JUnit test to getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test to updateEmployee method
    @DisplayName("JUnit test to updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given  
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("raja@gmail.com");
        employee.setFirstName("Raja");
        // when  
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then 
        assertThat(updatedEmployee.getEmail()).isEqualTo("raja@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Raja");
    }

    // JUnit test to deleteEmployee method
    @DisplayName("JUnit test to deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given  
        long employeeId = 1L;

        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when  
        employeeService.deleteEmployee(employeeId);

        // then 
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
