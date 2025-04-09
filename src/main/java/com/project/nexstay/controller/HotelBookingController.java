package com.project.nexstay.controller;

import com.project.nexstay.dto.BookingDto;
import com.project.nexstay.dto.BookingRequest;
import com.project.nexstay.dto.GuestDto;
import com.project.nexstay.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping(path="/init")
    public ResponseEntity<BookingDto> initiateBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initiateBooking(bookingRequest));
    }
    @PostMapping(path="/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }

    @PostMapping(path="/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initiatePayment(@PathVariable Long bookingId){
        String sessionUrl=bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl",sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/status")
    public ResponseEntity<Map<String,String>> getBookingStatus(@PathVariable Long bookingId){
        return ResponseEntity.ok(Map.of("status",bookingService.getBookingStatus(bookingId)));
    }

}
