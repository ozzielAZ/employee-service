package com.invex.example.employee.service;

import com.invex.example.employee.dto.EmployeeDTO;
import com.invex.example.employee.exception.EmployeesApiException;
import com.invex.example.employee.exception.ResourceBoundleException;
import com.invex.example.employee.mapper.EmployeeMapper;
import com.invex.example.employee.model.Employee;
import com.invex.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    // --- Tests para createEmployees ---
    @Test
    void createEmployees_ShouldThrowException_WhenListIsNull() {
        ResourceBoundleException thrown = assertThrows(ResourceBoundleException.class, () -> {
            employeeService.createEmployees(null);
        });
        assertEquals(EmployeesApiException.LIST_EMPTY.getErrorCode(), thrown.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void createEmployees_ShouldThrowException_WhenListIsEmpty() {
        ResourceBoundleException thrown = assertThrows(ResourceBoundleException.class, () -> {
            employeeService.createEmployees(Collections.emptyList());
        });
        assertEquals(EmployeesApiException.LIST_EMPTY.getErrorCode(), thrown.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getHttpStatus());
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void createEmployees_ShouldReturnSavedEmployees_WhenSuccessful() {
        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setFirstName("John");
        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setFirstName("Jane");
        List<EmployeeDTO> dtos = Arrays.asList(dto1, dto2);

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = Arrays.asList(employee1, employee2);
        
        Employee savedEmployee1 = new Employee();
        savedEmployee1.setId(1);
        Employee savedEmployee2 = new Employee();
        savedEmployee2.setId(2);
        List<Employee> savedEmployees = Arrays.asList(savedEmployee1, savedEmployee2);

        when(employeeMapper.employeeDTOToEmployee(any(EmployeeDTO.class)))
            .thenReturn(employee1, employee2);
        
        when(repository.saveAll(anyList())).thenReturn(savedEmployees);
        
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class)))
            .thenReturn(new EmployeeDTO(), new EmployeeDTO());

        List<EmployeeDTO> result = employeeService.createEmployees(dtos);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeMapper, times(2)).employeeDTOToEmployee(any(EmployeeDTO.class));
        verify(repository, times(1)).saveAll(anyList());
        verify(employeeMapper, times(2)).employeeToEmployeeDTO(any(Employee.class));
    }

    // --- Tests para getAllEmployees ---
    @Test
    void getAllEmployees_ShouldReturnEmptyList_WhenNoEmployeesFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees_WhenFound() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        Employee employee2 = new Employee();
        employee2.setId(2);
        List<Employee> employees = Arrays.asList(employee1, employee2);

        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setId(1);
        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setId(2);

        when(repository.findAll()).thenReturn(employees);
        when(employeeMapper.employeeToEmployeeDTO(employee1)).thenReturn(dto1);
        when(employeeMapper.employeeToEmployeeDTO(employee2)).thenReturn(dto2);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(repository, times(1)).findAll();
        verify(employeeMapper, times(2)).employeeToEmployeeDTO(any(Employee.class));
    }

    // --- Tests para getEmployeeById ---
    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenFound() {
        Integer employeeId = 1;
        Employee employee = new Employee();
        employee.setId(employeeId);
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employeeId);

        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(dto);

        EmployeeDTO result = employeeService.getEmployeeById(employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
        verify(repository, times(1)).findById(employeeId);
        verify(employeeMapper, times(1)).employeeToEmployeeDTO(employee);
    }

    @Test
    void getEmployeeById_ShouldThrowException_WhenNotFound() {
        Integer employeeId = 99;
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceBoundleException thrown = assertThrows(ResourceBoundleException.class, () -> {
            employeeService.getEmployeeById(employeeId);
        });
        assertEquals(EmployeesApiException.NOTFOUND_EMPLOYEE.getErrorCode(), thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
        verify(repository, times(1)).findById(employeeId);
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }

    // --- Tests para updateEmployee ---
    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenSuccessful() {
        Integer employeeId = 1;
        EmployeeDTO inputDto = new EmployeeDTO();
        inputDto.setFirstName("Updated John");
        inputDto.setLastName("Updated Doe");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setFirstName("Old John");
        existingEmployee.setLastName("Old Doe");

        Employee updatedEntity = new Employee();
        updatedEntity.setId(employeeId);
        updatedEntity.setFirstName("Updated John");
        updatedEntity.setLastName("Updated Doe");

        EmployeeDTO outputDto = new EmployeeDTO();
        outputDto.setId(employeeId);
        outputDto.setFirstName("Updated John");

        when(repository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        doNothing().when(employeeMapper).updateEmployeeFromDto(inputDto, existingEmployee);
        when(repository.save(any(Employee.class))).thenReturn(updatedEntity);
        when(employeeMapper.employeeToEmployeeDTO(updatedEntity)).thenReturn(outputDto);

        EmployeeDTO result = employeeService.updateEmployee(employeeId, inputDto);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
        assertEquals("Updated John", result.getFirstName());
        verify(repository, times(1)).findById(employeeId);
        verify(employeeMapper, times(1)).updateEmployeeFromDto(inputDto, existingEmployee);
        verify(repository, times(1)).save(existingEmployee);
        verify(employeeMapper, times(1)).employeeToEmployeeDTO(updatedEntity);
    }

    @Test
    void updateEmployee_ShouldThrowException_WhenEmployeeNotFound() {
        Integer employeeId = 99;
        EmployeeDTO inputDto = new EmployeeDTO();
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        ResourceBoundleException thrown = assertThrows(ResourceBoundleException.class, () -> {
            employeeService.updateEmployee(employeeId, inputDto);
        });
        assertEquals(EmployeesApiException.NOTFOUND_EMPLOYEE.getErrorCode(), thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
        verify(repository, times(1)).findById(employeeId);
        verify(employeeMapper, never()).updateEmployeeFromDto(any(EmployeeDTO.class), any(Employee.class));
        verify(repository, never()).save(any(Employee.class));
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }

    // --- Tests para deleteEmployee ---
    @Test
    void deleteEmployee_ShouldDeleteSuccessfully_WhenEmployeeExists() {
        Integer employeeId = 1;
        when(repository.existsById(employeeId)).thenReturn(true);
        doNothing().when(repository).deleteById(employeeId);

        employeeService.deleteEmployee(employeeId);

        verify(repository, times(1)).existsById(employeeId);
        verify(repository, times(1)).deleteById(employeeId);
    }

    @Test
    void deleteEmployee_ShouldThrowException_WhenEmployeeNotFound() {
        Integer employeeId = 99;
        when(repository.existsById(employeeId)).thenReturn(false);

        ResourceBoundleException thrown = assertThrows(ResourceBoundleException.class, () -> {
            employeeService.deleteEmployee(employeeId);
        });
        assertEquals(EmployeesApiException.NOTFOUND_EMPLOYEE.getErrorCode(), thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getHttpStatus());
        verify(repository, times(1)).existsById(employeeId);
        verify(repository, never()).deleteById(any(Integer.class));
    }

    // --- Tests para searchEmployeesByFullText ---
    @Test
    void searchEmployeesByFullText_ShouldReturnResults_WhenFound() {
        String searchTerm = "John Doe";
        String preparedSearchTerm = "+John* +Doe*";
        
        Employee employee1 = new Employee();
        employee1.setId(1);
        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setId(1);

        List<Employee> employees = Collections.singletonList(employee1);
        List<EmployeeDTO> dtos = Collections.singletonList(dto1);

        when(repository.searchEmployeesByFullText(preparedSearchTerm)).thenReturn(employees);
        when(employeeMapper.employeeToEmployeeDTO(employee1)).thenReturn(dto1);

        List<EmployeeDTO> result = employeeService.searchEmployeesByFullText(searchTerm);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(repository, times(1)).searchEmployeesByFullText(preparedSearchTerm);
        verify(employeeMapper, times(1)).employeeToEmployeeDTO(employee1);
    }

    @Test
    void searchEmployeesByFullText_ShouldReturnEmptyList_WhenSearchTermIsNull() {
        // Configuración: El repo devolverá lista vacía cuando se le pase el término vacío
        when(repository.searchEmployeesByFullText("")).thenReturn(Collections.emptyList());

        // Ejecución con término nulo
        List<EmployeeDTO> result = employeeService.searchEmployeesByFullText(null);

        // Verificación
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Ahora verificamos que el repositorio FUE llamado con una cadena vacía
        verify(repository, times(1)).searchEmployeesByFullText("");
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }

    @Test
    void searchEmployeesByFullText_ShouldReturnEmptyList_WhenSearchTermIsEmpty() {
        // Configuración
        when(repository.searchEmployeesByFullText("")).thenReturn(Collections.emptyList());

        // Ejecución con término vacío
        List<EmployeeDTO> result = employeeService.searchEmployeesByFullText("");

        // Verificación
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).searchEmployeesByFullText("");
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }

    @Test
    void searchEmployeesByFullText_ShouldReturnEmptyList_WhenSearchTermIsSpaces() {
        // Configuración
        when(repository.searchEmployeesByFullText("")).thenReturn(Collections.emptyList());

        // Ejecución con solo espacios
        List<EmployeeDTO> result = employeeService.searchEmployeesByFullText("   ");

        // Verificación
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).searchEmployeesByFullText("");
        verify(employeeMapper, never()).employeeToEmployeeDTO(any(Employee.class));
    }
}