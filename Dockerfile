# ----- Fase de Construcción (Build Stage) -----
# Usa una imagen base con JDK 11 y Maven para compilar la aplicación.
FROM maven:3.9.6-eclipse-temurin-11 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración del proyecto (pom.xml, etc.) para descargar dependencias primero
COPY pom.xml .

# Descarga las dependencias (optimiza el cache de Docker)
RUN mvn dependency:go-offline

# Copia el resto del código fuente, incluyendo src/main/resources/test-employee.p12
COPY src ./src

# Compila la aplicación Spring Boot en un JAR ejecutable
RUN mvn clean package -DskipTests

# ----- Fase de Ejecución (Run Stage) -----
# Usa una imagen base más ligera que solo contenga JRE 11
FROM eclipse-temurin:11-jre-focal

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR ejecutable de la fase de construcción
# Asegúrate de que el nombre del JAR coincida con el que genera tu Maven/Gradle
# Normalmente es 'nombre-de-tu-artefacto-VERSION.jar' (ej. employee-1.0.0.jar)
COPY --from=build /app/target/employee-1.0.0.jar app.jar

# Expone el puerto en el que tu aplicación Spring Boot escuchará HTTPS
EXPOSE 8881 

# Define el comando para ejecutar la aplicación cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]