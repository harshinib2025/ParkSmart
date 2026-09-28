package com.example.parksmart.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.parksmart.entity.CheckInOut;
import com.example.parksmart.service.CheckInOutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/check-in-out")
public class CheckInOutController {

    private final CheckInOutService checkInOutService;

    public CheckInOutController(CheckInOutService checkInOutService) {
        this.checkInOutService = checkInOutService;
    }

    @PostMapping("/check-in/{bookingId}")
    public CheckInOut checkIn(@PathVariable Long bookingId) {
        return checkInOutService.checkIn(bookingId);
    }

    @PostMapping("/check-out/{checkInOutId}")
    public CheckInOut checkOut(@PathVariable Long checkInOutId) {
        return checkInOutService.checkOut(checkInOutId);
    }

    @GetMapping
    public List<CheckInOut> getAllCheckIns() {
        return checkInOutService.getAllCheckIns();
    }
}