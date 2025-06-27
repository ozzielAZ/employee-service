-- Asegurarse de que la base de datos exista y usarla
CREATE DATABASE IF NOT EXISTS test_invex;
USE test_invex;

-- Crear la tabla 'employee' si no existe
CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    maternal_last_name VARCHAR(50),
    age INT,
    gender VARCHAR(20),
    birth_date DATE,
    job_title VARCHAR(100), 
    hire_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    -- Añadir el índice FULLTEXT para búsquedas avanzadas
     INDEX (first_name, middle_name, last_name, maternal_last_name)
);

ALTER TABLE employee ADD FULLTEXT(first_name, middle_name, last_name, maternal_last_name);

-- Crear el usuario 'employees_dev' si no existe y asignarle una contraseña
CREATE USER IF NOT EXISTS 'employees_dev'@'%' IDENTIFIED BY 'Emp1oy33Des@';

-- Otorgar privilegios al nuevo usuario sobre la tabla 'employee'
GRANT SELECT, INSERT, UPDATE, DELETE ON test_invex.employee TO 'employees_dev'@'%';

-- Aplicar los cambios de privilegios
FLUSH PRIVILEGES;;