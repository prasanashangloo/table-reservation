// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.repo;

import com.example.reservation.model.Booking;
import com.example.reservation.model.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByIdDesc(Long userId);
    List<Booking> findByBookingDateAndStatusNot(LocalDate date, BookingStatus status);
    boolean existsByTableIdAndBookingDateAndSlotAndStatusNot(
            Long tableId, LocalDate date, String slot, BookingStatus status);
}
