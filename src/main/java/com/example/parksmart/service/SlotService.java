package com.example.parksmart.service;

import com.example.parksmart.dto.OccupancyResponse;
import com.example.parksmart.entity.Slot;
import com.example.parksmart.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Slot createSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }
    public long getTotalSlots() {
        return slotRepository.count();
    }
    public long getAvailableSlots() {
        return slotRepository.findAll()
        .stream()
        .filter(Slot::isAvailable)
        .count();
    }
    public OccupancyResponse getOccupancy() {

    long totalSlots = slotRepository.count();

    long availableSlots = slotRepository.findAll()
            .stream()
            .filter(Slot::isAvailable)
            .count();

    long occupiedSlots = totalSlots - availableSlots;

    return new OccupancyResponse(
            totalSlots,
            occupiedSlots,
            availableSlots
    );
}

}