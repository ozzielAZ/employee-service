package com.invex.example.employee.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object (DTO) for employee records")
public class EmployeeDTO {

    @Schema(description = "Unique identifier of the employee", example = "1")
    private Integer id;

    @Schema(description = "Employee's first name", example = "John", required = true)
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;

    @Schema(description = "Employee's middle name (optional)", example = "Michael", nullable = true)
    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @Schema(description = "Employee's last name", example = "Doe", required = true)
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
    private String lastName;

    @Schema(description = "Employee's maternal last name (optional)", example = "Smith", nullable = true)
    @Size(max = 50, message = "Maternal last name cannot exceed 50 characters")
    private String maternalLastName;

    @Schema(description = "Employee's gender", example = "Male", required = true)
    @NotBlank(message = "Gender cannot be empty")
    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    private String gender;

    @Schema(description = "Employee's age (auto-calculated)", example = "30", nullable = true)
    private Integer age;

    @Schema(description = "Employee's birth date (format: dd-MM-yyyy)", example = "15-05-1990", required = true)
    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(description = "Employee's job position", example = "Software Engineer", required = true)
    @NotBlank(message = "Job title cannot be empty")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    private String jobTitle;

    @Schema(description = "Whether the employee is active", example = "true", required = true)
    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;
}