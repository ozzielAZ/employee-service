package com.invex.example.employee.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invex.example.employee.dto.EmployeeDTO;
import com.invex.example.employee.exception.EmployeesApiException;
import com.invex.example.employee.mapper.EmployeeMapper;
import com.invex.example.employee.model.Employee;
import com.invex.example.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private EmployeeMapper employeeMapper;

	// Operación: Crear un empleado (CREATE)
	@Transactional
	public List<EmployeeDTO> createEmployees(List<EmployeeDTO> employeeList) {
		if (employeeList == null || employeeList.isEmpty()) {
			throw EmployeesApiException.LIST_EMPTY;
		}

		List<Employee> employees = employeeList.stream().map(employeeMapper::employeeDTOToEmployee)
				.collect(Collectors.toList());

		List<Employee> savedEmployees = repository.saveAll(employees);

		return savedEmployees.stream().map(employeeMapper::employeeToEmployeeDTO).collect(Collectors.toList());
	}

	// Operación: Obtener todos los empleados (READ ALL)
	@Transactional(readOnly = true)
	public List<EmployeeDTO> getAllEmployees() {
		List<Employee> employees = repository.findAll();
		return employees.stream().map(employeeMapper::employeeToEmployeeDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public EmployeeDTO getEmployeeById(Integer id) {
		return employeeMapper.employeeToEmployeeDTO(
				repository.findById(id).orElseThrow(() -> EmployeesApiException.NOTFOUND_EMPLOYEE));
	}

	// Operación: Actualizar un empleado (UPDATE)
	@Transactional
	public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO) {
		return repository.findById(id).map(existingEmployee -> {
			employeeMapper.updateEmployeeFromDto(employeeDTO, existingEmployee);
			Employee updatedEmployee = repository.save(existingEmployee);
			return employeeMapper.employeeToEmployeeDTO(updatedEmployee);
		}).orElseThrow(() -> EmployeesApiException.NOTFOUND_EMPLOYEE);
	}

	// Operación: Eliminar un empleado (DELETE)
	@Transactional
	public void deleteEmployee(Integer id) {
		if (!repository.existsById(id)) {
			throw EmployeesApiException.NOTFOUND_EMPLOYEE;
		}
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<EmployeeDTO> searchEmployeesByFullText(String name) {
		String preparedSearchTerm = prepareSearchTermForMySqlFullText(name);

		List<Employee> employees = repository.searchEmployeesByFullText(preparedSearchTerm);

		return employees.stream().map(employeeMapper::employeeToEmployeeDTO).collect(Collectors.toList());
	}

	private String prepareSearchTermForMySqlFullText(String originalTerm) {
		// ... (Tu lógica existente para preparar el término para MySQL) ...
		// Asegúrate de que esta función aún maneje `originalTerm` correctamente incluso
		// si ya está validado.
		// Por ejemplo, si originalTerm.trim().isEmpty() devuelve true, puede retornar
		// una cadena vacía o lo que sea apropiado para tu query.
		if (originalTerm == null || originalTerm.trim().isEmpty()) {
			return ""; // Aunque el controlador ya valida, es buena práctica defensiva.
		}
		String cleanedTerm = originalTerm.replaceAll("[\\-\\+<>\\(\\)\"~*@]", "");
		String[] words = cleanedTerm.trim().split("\\s+");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				result.append("+").append(word).append("* ");
			}
		}
		return result.toString().trim();
	}

}
