// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.service;

import com.example.reservation.model.Booking;
import com.example.reservation.model.BookingStatus;
import com.example.reservation.model.RestaurantTable;
import com.example.reservation.repo.BookingRepository;
import com.example.reservation.repo.TableRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    public static final List<String> SLOTS =
            List.of("12:00", "13:00", "14:00", "18:00", "19:00", "20:00", "21:00");

    private final BookingRepository bookings;
    private final TableRepository tables;

    public BookingService(BookingRepository bookings, TableRepository tables) {
        this.bookings = bookings;
        this.tables = tables;
    }

    public record SlotView(String slot, boolean available) {}
    public record TableAvailability(Long tableId, String tableName, int capacity, List<SlotView> slots) {}

    public List<TableAvailability> availability(LocalDate date, int partySize) {
        Set<String> taken = bookings.findByBookingDateAndStatusNot(date, BookingStatus.CANCELLED)
                .stream().map(b -> b.getTableId() + "|" + b.getSlot()).collect(Collectors.toSet());
        List<TableAvailability> result = new ArrayList<>();
        for (RestaurantTable t : tables.findAll()) {
            if (t.getCapacity() < partySize) continue;
            List<SlotView> views = SLOTS.stream()
                    .map(s -> new SlotView(s, !taken.contains(t.getId() + "|" + s)))
                    .toList();
            result.add(new TableAvailability(t.getId(), t.getName(), t.getCapacity(), views));
        }
        return result;
    }

    @Transactional
    public Booking create(Long userId, Long tableId, LocalDate date, String slot, int partySize) {
        if (date.isBefore(LocalDate.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Date cannot be in the past");
        }
        if (!SLOTS.contains(slot)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Unknown time slot");
        }
        RestaurantTable table = tables.findById(tableId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Table not found"));
        if (partySize < 1 || partySize > table.getCapacity()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Party size exceeds table capacity");
        }
        if (bookings.existsByTableIdAndBookingDateAndSlotAndStatusNot(
                tableId, date, slot, BookingStatus.CANCELLED)) {
            throw new ApiException(HttpStatus.CONFLICT, "That table and slot is already booked");
        }
        return bookings.save(new Booking(userId, tableId, date, slot, partySize));
    }

    public List<Booking> mine(Long userId) {
        return bookings.findByUserIdOrderByIdDesc(userId);
    }

    public Booking get(Long userId, Long id) {
        Booking b = bookings.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!b.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Not your booking");
        }
        return b;
    }

    @Transactional
    public Booking confirm(Long userId, Long id) {
        Booking b = get(userId, id);
        if (b.getStatus() != BookingStatus.PENDING) {
            throw new ApiException(HttpStatus.CONFLICT, "Only PENDING bookings can be confirmed");
        }
        b.setStatus(BookingStatus.CONFIRMED);
        return bookings.save(b);
    }

    @Transactional
    public Booking cancel(Long userId, Long id) {
        Booking b = get(userId, id);
        if (b.getStatus() == BookingStatus.CANCELLED) {
            throw new ApiException(HttpStatus.CONFLICT, "Booking is already cancelled");
        }
        b.setStatus(BookingStatus.CANCELLED);
        return bookings.save(b);
    }
}
