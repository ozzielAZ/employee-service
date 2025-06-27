package com.invex.example.employee.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invex.example.employee.dto.EmployeeDTO;
import com.invex.example.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/employees")
@Validated
@Tag(name = "Employee Management", description = "APIs for managing employee records")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping
	@Operation(summary = "Get all employees", description = "Retrieves a list of all registered employees")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved employee list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))))
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get employee by ID", description = "Fetches a single employee by their unique ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer id) {
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}

	@PostMapping
	@Operation(summary = "Create employees", description = "Adds one or more new employees to the system")
	@ApiResponse(responseCode = "201", description = "Employees created successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))))
	public ResponseEntity<List<EmployeeDTO>> createEmployees(@Valid @RequestBody List<EmployeeDTO> employeeDTOs) {
		List<EmployeeDTO> createdEmployees = employeeService.createEmployees(employeeDTOs);
		return new ResponseEntity<>(createdEmployees, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update employee", description = "Modifies an existing employee's details")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id,
			@Valid @RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete employee", description = "Removes an employee from the system")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/search")
	@Operation(summary = "Search employees", description = "Finds employees by name using full-text search (minimum 2 characters)")
	@ApiResponse(responseCode = "200", description = "List of matching employees", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))))
	public ResponseEntity<List<EmployeeDTO>> searchEmployees(
			@RequestParam(name = "name") @Size(min = 2, message = "Search term must be at least 2 characters long") String name) {
		List<EmployeeDTO> foundEmployees = employeeService.searchEmployeesByFullText(name);
		return new ResponseEntity<>(foundEmployees, HttpStatus.OK);
	}

}
