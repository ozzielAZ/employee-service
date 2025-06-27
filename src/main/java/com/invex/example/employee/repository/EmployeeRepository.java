package com.invex.example.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.invex.example.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	// Opcional: Buscar en primer nombre, segundo nombre, apellido o apellido materno
	List<Employee> findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrMaternalLastNameContainingIgnoreCase(
			String firstName, String middleName, String lastName, String maternalLastName);
	
    // Usa 'MATCH AGAINST ... IN BOOLEAN MODE'
    @Query(value = "SELECT * FROM employee e WHERE MATCH (e.first_name, e.middle_name, e.last_name, e.maternal_last_name) AGAINST (:searchTerm IN BOOLEAN MODE)",
           nativeQuery = true)
    List<Employee> searchEmployeesByFullText(@Param("searchTerm") String searchTerm);

    // Opcional: Búsqueda con ordenamiento por relevancia (score)
    @Query(value = "SELECT *, MATCH(first_name, middle_name, last_name, maternal_last_name) AGAINST (:searchTerm IN BOOLEAN MODE) AS score FROM employee e WHERE MATCH(e.first_name, e.middle_name, e.last_name, e.maternal_last_name) AGAINST (:searchTerm IN BOOLEAN MODE) ORDER BY score DESC",
           nativeQuery = true)
    List<Employee> searchEmployeesByFullTextRanked(@Param("searchTerm") String searchTerm);
}
