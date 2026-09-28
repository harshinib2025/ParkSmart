package com.example.parksmart.repository;

import com.example.parksmart.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT b FROM Booking b
        WHERE b.slot.id = :slotId
          AND b.startTime < :endTime
          AND b.endTime > :startTime
          AND b.status <> 'CANCELLED'
    """)
    List<Booking> findOverlappingBookings(
            @Param("slotId") Long slotId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}