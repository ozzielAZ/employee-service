package com.invex.example.employee.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class Employee {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "maternal_last_name", length = 50)
    private String maternalLastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "hire_date", updatable = false)
    private LocalDateTime hireDate = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;
}
