package com.project.nexstay.service;

import com.project.nexstay.dto.HotelPriceDto;
import com.project.nexstay.dto.HotelSearchRequest;
import com.project.nexstay.dto.InventoryDto;
import com.project.nexstay.dto.UpdateInventoryRequestDto;
import com.project.nexstay.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventoryByRoom(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
