# Library Manager
Backend

## Requisitos

Java 21 
Spring Boot 3.x (Web, Data JPA, Validation)
PostgreSQL como motor de base de datos 
JUnit 5 & Mockito para pruebas unitarias e integración 

## Configuración

1. Requisitos Previos
JDK 21 instalado 

PostgreSQL corriendo localmente 

Node.js y Angular CLI (para el frontend)

2. Base de Datos
Crea la base de datos necesaria en PostgreSQL:

SQL
CREATE DATABASE library_db;

3. Configuración del Backend
Asegúrate de que el archivo src/main/resources/application.properties tenga las credenciales correctas:

Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=tu_usuario 
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
Ejecuta el servidor:

Bash
./mvnw spring-boot:run
El servidor iniciará en: http://localhost:8080.

4. Configuración del Frontend
Navega a la carpeta del frontend e instala las dependencias:

Bash
npm install
ng serve
Accede a: http://localhost:4200.

## API Endpoints
- GET /api/books - Listar libros
- POST /api/books - Crear libro
- POST /api/loans - Crear préstamo



