package com.example.parksmart.service;

import com.example.parksmart.entity.Booking;
import com.example.parksmart.entity.CheckInOut;
import com.example.parksmart.entity.Slot;
import com.example.parksmart.repository.BookingRepository;
import com.example.parksmart.repository.CheckInOutRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInOutService {

    private static final double PENALTY_PER_HOUR = 50.0;

    private final CheckInOutRepository checkInOutRepository;
    private final BookingRepository bookingRepository;

    public CheckInOutService(CheckInOutRepository checkInOutRepository,
                             BookingRepository bookingRepository) {
        this.checkInOutRepository = checkInOutRepository;
        this.bookingRepository = bookingRepository;
    }

    public CheckInOut checkIn(Long bookingId) {

    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    if (checkInOutRepository.findByBookingId(bookingId).isPresent()) {
        throw new RuntimeException("Booking has already been checked in");
    }

    CheckInOut checkInOut = new CheckInOut(
            booking,
            LocalDateTime.now()
    );

    return checkInOutRepository.save(checkInOut);
}

    public CheckInOut checkOut(Long checkInOutId) {

        CheckInOut checkInOut = checkInOutRepository.findById(checkInOutId)
                .orElseThrow(() -> new RuntimeException("Check-in record not found"));

        LocalDateTime checkOutTime = LocalDateTime.now();

        checkInOut.setCheckOutTime(checkOutTime);

        LocalDateTime bookedEndTime =
                checkInOut.getBooking().getEndTime();

        if (checkOutTime.isAfter(bookedEndTime)) {

            long extraMinutes = Duration.between(
                    bookedEndTime,
                    checkOutTime
            ).toMinutes();

            long extraHours = (long) Math.ceil(extraMinutes / 60.0);

            double penalty = extraHours * PENALTY_PER_HOUR;

            checkInOut.setPenalty(penalty);

        } else {
            checkInOut.setPenalty(0.0);
        }

        // Make the parking slot available again
        Slot slot = checkInOut.getBooking().getSlot();
        slot.setAvailable(true);

        return checkInOutRepository.save(checkInOut);
    }

    public List<CheckInOut> getAllCheckIns() {
        return checkInOutRepository.findAll();
    }
}