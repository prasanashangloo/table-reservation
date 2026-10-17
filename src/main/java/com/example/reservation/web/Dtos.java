// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public final class Dtos {
    private Dtos() {}

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password) {}

    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}

    public record BookingRequest(
            @NotNull Long tableId,
            @NotNull LocalDate date,
            @NotBlank String slot,
            @Min(1) int partySize) {}
}
