package com.example.parksmart.dto;

public class OccupancyResponse {

    private long totalSlots;
    private long occupiedSlots;
    private long availableSlots;

    public OccupancyResponse(long totalSlots,
                             long occupiedSlots,
                             long availableSlots) {
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = availableSlots;
    }

    public long getTotalSlots() {
        return totalSlots;
    }

    public long getOccupiedSlots() {
        return occupiedSlots;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }
}