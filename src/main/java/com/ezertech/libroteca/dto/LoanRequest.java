package com.ezertech.libroteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Record: Una forma concisa de definir clases inmutables (Java 16+)
public record LoanRequest(
        @NotNull(message = "El ID del libro es obligatorio")
        Long bookId,

        @NotBlank(message = "El nombre del solicitante es obligatorio")
        String borrowerName,

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        String borrowerEmail
) {}