// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long tableId;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private String slot;

    @Column(nullable = false)
    private int partySize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Booking() {}

    public Booking(Long userId, Long tableId, LocalDate bookingDate, String slot, int partySize) {
        this.userId = userId;
        this.tableId = tableId;
        this.bookingDate = bookingDate;
        this.slot = slot;
        this.partySize = partySize;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getTableId() { return tableId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public String getSlot() { return slot; }
    public int getPartySize() { return partySize; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
}
