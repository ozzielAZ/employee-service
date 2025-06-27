package com.invex.example.employee.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmployeeAgeValidator.class) // Apunta a tu clase validadora
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Puede ser aplicada a campos y parámetros
@Retention(RetentionPolicy.RUNTIME) // Disponible en tiempo de ejecución
public @interface ValidEmployeeAge {

    String message() default "Employee must be 18 years or older based on birth date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}