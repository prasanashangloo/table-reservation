// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    public RestaurantTable() {}

    public RestaurantTable(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
}
