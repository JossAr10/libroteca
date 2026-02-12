package com.ezertech.libroteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id autoincremental
    private Long id;

    @NotNull(message = "El título es obligatorio")
    private String title;

    @NotNull(message = "El autor es obligatorio")
    private String author;

    @Column(unique = true, nullable = false, length = 13) // Restricción de base de datos
    @NotNull(message = "El ISBN es obligatorio")
    @Size(min = 13, max = 13, message = "El ISBN debe tener exactamente 13 caracteres")
    private String isbn;

    @Min(value = 1000, message = "El año debe ser mayor a 1000")
    @Max(value = 2026, message = "El año no puede ser futuro") // Validación requerida hasta el año actual
    @Column(name = "publication_year")
    private Integer publicationYear;

    @Enumerated(EnumType.STRING) // Guarda el nombre "AVAILABLE" en la BD en lugar de un número
    @Column(nullable = false)
    private BookStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructor vacío (requerido por JPA)
    public Book() {
    }

    // Ciclo de vida para asignar fecha automáticamente antes de guardar
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // El ejercicio 4 menciona establecer status = AVAILABLE al registrar
        if (this.status == null) {
            this.status = BookStatus.AVAILABLE;
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}