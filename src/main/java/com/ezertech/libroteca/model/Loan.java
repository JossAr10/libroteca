package com.ezertech.libroteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // Relación obligatoria: Un préstamo debe tener un libro
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "El libro es obligatorio")
    private Book book;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Column(nullable = false)
    private String borrowerName;

    @Email(message = "Debe proporcionar un email válido") // Valida formato de correo
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false)
    private String borrowerEmail;

    @NotNull(message = "La fecha de préstamo es obligatoria")
    @Column(nullable = false)
    private LocalDate loanDate;

    @NotNull(message = "La fecha de devolución esperada es obligatoria")
    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate; // Puede ser nulo (null = no devuelto aún)

    // Constructor vacío
    public Loan() {
    }

    // Metodo helper solicitado explícitamente en el PDF
    // Retorna true si la fecha límite ya pasó y el libro no ha sido devuelto.
    public boolean isOverdue() {
        return returnDate == null && dueDate.isBefore(LocalDate.now());
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}