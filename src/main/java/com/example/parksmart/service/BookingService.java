package com.example.parksmart.service;

import com.example.parksmart.entity.Booking;
import com.example.parksmart.entity.Slot;
import com.example.parksmart.exception.BadRequestException;
import com.example.parksmart.repository.BookingRepository;
import com.example.parksmart.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;

    public BookingService(BookingRepository bookingRepository,
                           SlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
    }

    public Booking createBooking(Booking booking) {

        if (booking.getSlot() == null || booking.getSlot().getId() == null) {
            throw new BadRequestException("Slot is required");
        }

        Long slotId = booking.getSlot().getId();

        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw new BadRequestException("Start time and end time are required");
        }

        if (!booking.getEndTime().isAfter(booking.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }

        if (booking.getVehicleNumber() == null ||
                booking.getVehicleNumber().isBlank()) {
            throw new BadRequestException("Vehicle number is required");
        }

        // Find the actual slot from database
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new BadRequestException("Slot not found"));

        // Check whether slot is currently occupied
        if (!slot.isAvailable()) {
            throw new BadRequestException("Slot is currently occupied");
        }

        // Check overlapping bookings
        List<Booking> overlappingBookings =
                bookingRepository.findOverlappingBookings(
                        slotId,
                        booking.getStartTime(),
                        booking.getEndTime()
                );

        if (!overlappingBookings.isEmpty()) {
            throw new BadRequestException(
                    "Slot is already booked for the selected time"
            );
        }

        // Attach actual database slot
        booking.setSlot(slot);

        // Default status
        if (booking.getStatus() == null ||
                booking.getStatus().isBlank()) {
            booking.setStatus("BOOKED");
        }

        // Mark slot as occupied
        slot.setAvailable(false);
        slotRepository.save(slot);

        // Save booking
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}