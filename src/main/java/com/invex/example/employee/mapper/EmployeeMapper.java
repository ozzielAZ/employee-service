package com.invex.example.employee.mapper;

import java.time.LocalDate;
import java.time.Period;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.invex.example.employee.dto.EmployeeDTO;
import com.invex.example.employee.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "age", source = "birthDate", qualifiedByName = "calculateAge")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true) // Ignorar el campo age al mapear DTO a Entity
    @Mapping(target = "hireDate", ignore = true)
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO dto, @MappingTarget Employee entity);

    // Método para calcular la edad, referenciado por @Named
    @Named("calculateAge")
    default Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
