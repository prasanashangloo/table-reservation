// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.service;

import com.example.reservation.model.RestaurantTable;
import com.example.reservation.repo.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Seeds the restaurant floor plan on first start. */
@Component
public class DataInitializer implements CommandLineRunner {
    private final TableRepository tables;

    public DataInitializer(TableRepository tables) {
        this.tables = tables;
    }

    @Override
    public void run(String... args) {
        if (tables.count() > 0) return;
        tables.save(new RestaurantTable("Window 1", 2));
        tables.save(new RestaurantTable("Window 2", 2));
        tables.save(new RestaurantTable("Booth 1", 4));
        tables.save(new RestaurantTable("Booth 2", 4));
        tables.save(new RestaurantTable("Family 1", 6));
        tables.save(new RestaurantTable("Banquet", 8));
    }
}
