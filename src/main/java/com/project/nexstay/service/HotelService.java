package com.project.nexstay.service;

import com.project.nexstay.dto.HotelDto;
import com.project.nexstay.dto.HotelInfoDto;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id,HotelDto hotelDto);

    void activateHotelById(Long id);

    void deleteHotelById(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
