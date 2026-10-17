// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.web;

import com.example.reservation.model.Booking;
import com.example.reservation.service.AuthService;
import com.example.reservation.service.BookingService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingController {
    private static final String TOKEN = "X-Auth-Token";

    private final BookingService service;
    private final AuthService auth;

    public BookingController(BookingService service, AuthService auth) {
        this.service = service;
        this.auth = auth;
    }

    @GetMapping("/availability")
    public List<BookingService.TableAvailability> availability(
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "2") int partySize) {
        return service.availability(date, partySize);
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@RequestHeader(value = TOKEN, required = false) String token,
                          @Valid @RequestBody Dtos.BookingRequest req) {
        Long userId = auth.requireUserId(token);
        return service.create(userId, req.tableId(), req.date(), req.slot(), req.partySize());
    }

    @GetMapping("/bookings")
    public List<Booking> mine(@RequestHeader(value = TOKEN, required = false) String token) {
        return service.mine(auth.requireUserId(token));
    }

    @GetMapping("/bookings/{id}")
    public Booking one(@RequestHeader(value = TOKEN, required = false) String token, @PathVariable Long id) {
        return service.get(auth.requireUserId(token), id);
    }

    @PostMapping("/bookings/{id}/confirm")
    public Booking confirm(@RequestHeader(value = TOKEN, required = false) String token, @PathVariable Long id) {
        return service.confirm(auth.requireUserId(token), id);
    }

    @PostMapping("/bookings/{id}/cancel")
    public Booking cancel(@RequestHeader(value = TOKEN, required = false) String token, @PathVariable Long id) {
        return service.cancel(auth.requireUserId(token), id);
    }
}
