package com.example.parksmart.controller;
import com.example.parksmart.dto.OccupancyResponse;
import com.example.parksmart.entity.Slot;
import com.example.parksmart.service.SlotService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public Slot createSlot(@RequestBody Slot slot) {
        return slotService.createSlot(slot);
    }

    @GetMapping
    public List<Slot> getAllSlots() {
        return slotService.getAllSlots();
    }
    @GetMapping("/count")
    public long getTotalSlots() {
        return slotService.getTotalSlots();
    }
    @GetMapping("/available-count")
    public long getAvailableSlots() {
        return slotService.getAvailableSlots();
    }
    @GetMapping("/occupancy")
    public OccupancyResponse getOccupancy() {
            return slotService.getOccupancy();
}
}