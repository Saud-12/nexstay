package com.project.nexstay.service;

import com.project.nexstay.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createNewRoomByHotel(RoomDto roomDto,Long hotelId);

    List<RoomDto> getAllRoomInHotel(Long hotelId);

    RoomDto getRoomById(Long id);

    void deleteRoomById(Long id);

    RoomDto updateRoomById(Long hotelId,Long roomId,RoomDto roomDto);
}
