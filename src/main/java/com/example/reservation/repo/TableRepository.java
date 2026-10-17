// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.repo;

import com.example.reservation.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
}
