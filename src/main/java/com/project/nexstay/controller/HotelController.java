package com.project.nexstay.controller;

import com.project.nexstay.dto.BookingDto;
import com.project.nexstay.dto.HotelDto;
import com.project.nexstay.dto.HotelReportDto;
import com.project.nexstay.service.BookingService;
import com.project.nexstay.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hoteldto){
        log.info("Attempting to create a new Hotel with name: {}",hoteldto.getName());
        return new ResponseEntity<>(hotelService.createNewHotel(hoteldto), HttpStatus.CREATED);
    }

    @GetMapping(path="/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId){
        log.info("Getting hotel by ID: {}",hotelId);
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId,@RequestBody HotelDto hotelDto){
        log.info("Attempting to update Hotel by ID: {}",hotelId);
        return ResponseEntity.ok(hotelService.updateHotelById(hotelId,hotelDto));
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotelById(@PathVariable Long hotelId){
        log.info("Attempting to activate Hotel by ID: {}",hotelId);
        hotelService.activateHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
        log.info("Attempting to delete Hotel by ID: {}",hotelId);
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId, @RequestParam(required = false) LocalDate startDate,@RequestParam(required = false) LocalDate endDate){
        if(startDate==null)  startDate=LocalDate.now().minusMonths(1);
        if(endDate==null) endDate=LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId,startDate,endDate));
    }
}