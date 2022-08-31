package com.karthik.springboot.repository;

import com.karthik.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.unit.jupiter.api.BeforeEach;
import org.unit.jupiter.api.DisplayName;
import org.unit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRespositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Karthik")
                .lastName("Pavan")
                .email("karthik@gmail,com")
                .build();
    }
    // unit test to save employee operation
    //@DisplayName("unit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given 
        Employee employee = Employee.builder()
                .firstName("Karthik")
                .lastName("Karthik")
                .email("karthik@gmail,com")
                .build();
        // when 
        Employee savedEmployee = employeeRepository.save(employee);

        // then  
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    // unit test to get all employees operation
    @DisplayName("unit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Karthik")
//                .email("karthik@gmail,com")
//                .build();

        Employee employee1 = Employee.builder()
                .firstName("seetha")
                .lastName("hy")
                .email("seetha@gmail,com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when  
        List<Employee> employeeList = employeeRepository.findAll();

        // then  
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // unit test to get employee by id operation
    @DisplayName("unit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Karthik")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);

        // when  
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then  
        assertThat(employeeDB).isNotNull();
    }

    // unit test to get employee by email operation
    @DisplayName("unit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);

        // when  
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then  
        assertThat(employeeDB).isNotNull();
    }

    // unit test for update employee operation
    @DisplayName("unit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);

        // when  
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("seetha@gmail.com");
        savedEmployee.setFirstName("Seetha");
        Employee updatedEmployee =  employeeRepository.save(savedEmployee);

        // then  
        assertThat(updatedEmployee.getEmail()).isEqualTo("seetha@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Seetha");
    }

    // unit test to delete employee operation
    @DisplayName("unit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);

        // when  
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then  
        assertThat(employeeOptional).isEmpty();
    }

    // unit test with custom query using JPQL & index
    @DisplayName("unit test for custom query using JPQL & index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);
        String firstName = "Karthik";
        String lastName = "Pavan";

        // when  
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then  
        assertThat(savedEmployee).isNotNull();
    }

    // unit test to custom query using JPQL with Named params
    @DisplayName("unit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);
        String firstName = "Karthik";
        String lastName = "Pavan";

        // when  
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then  
        assertThat(savedEmployee).isNotNull();
    }

    // unit test to custom query using native SQL with index
    @DisplayName("unit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);
        // String firstName = "Karthik";
        // String lastName = "Pavan";

        // when  
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then  
        assertThat(savedEmployee).isNotNull();
    }

    // unit test to custom query using native SQL with named params
    @DisplayName("unit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        // given 
//        Employee employee = Employee.builder()
//                .firstName("Karthik")
//                .lastName("Pavan")
//                .email("karthik@gmail,com")
//                .build();
        employeeRepository.save(employee);
        // String firstName = "Karthik";
        // String lastName = "Pavan";

        // when  
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());

        // then  
        assertThat(savedEmployee).isNotNull();
    }

}
