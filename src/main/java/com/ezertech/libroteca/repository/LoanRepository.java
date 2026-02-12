package com.ezertech.libroteca.repository;

import com.ezertech.libroteca.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // 1. Buscar préstamos por email del solicitante
    List<Loan> findByBorrowerEmail(String email);

    // 2. Préstamos activos (donde la fecha de devolución es nula)
    List<Loan> findByReturnDateIsNull();

    // 3. Préstamos vencidos
    // Selecciona préstamos donde la fecha esperada es anterior a hoy Y no se han devuelto
    @Query("SELECT l FROM Loan l WHERE " +
            "l.dueDate < CURRENT_DATE AND " +
            "l.returnDate IS NULL")
    List<Loan> findOverdueLoans();

    // 4. Verificar si un libro está prestado actualmente
    // Spring Data interpreta 'BookId' navegando la relación 'book' hacia su 'id'
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
}