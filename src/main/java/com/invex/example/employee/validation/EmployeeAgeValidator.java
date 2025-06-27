package com.invex.example.employee.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class EmployeeAgeValidator implements ConstraintValidator<ValidEmployeeAge, LocalDate> {

    private static final int MIN_AGE = 18; // Define la edad mínima

    @Override
    public void initialize(ValidEmployeeAge constraintAnnotation) {
        // Puedes inicializar algo si es necesario desde la anotación
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {            
            return true;
        }

        int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();
        return calculatedAge >= MIN_AGE;
    }
}