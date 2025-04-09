package com.project.nexstay.controller;

import com.project.nexstay.dto.RoomDto;
import com.project.nexstay.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {
    private final RoomService roomService;

    @PostMapping()
    public ResponseEntity<RoomDto> createNewRoomByHotel(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        return new ResponseEntity<>(roomService.createNewRoomByHotel(roomDto,hotelId), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<RoomDto>> getAllRoomsByHotel(@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getAllRoomInHotel(hotelId));
    }

    @GetMapping(path="/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId,@PathVariable Long hotelId){
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping(path="/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId,@PathVariable Long hotelId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long hotelId,@PathVariable Long roomId,@RequestBody RoomDto roomDto){
       return ResponseEntity.ok(roomService.updateRoomById(hotelId,roomId,roomDto));
    }
}
