package com.project.nexstay.service;

import com.project.nexstay.dto.HotelDto;
import com.project.nexstay.dto.HotelInfoDto;
import com.project.nexstay.dto.RoomDto;
import com.project.nexstay.entity.Hotel;
import com.project.nexstay.entity.Room;
import com.project.nexstay.entity.User;
import com.project.nexstay.exception.ResourceNotFoundException;
import com.project.nexstay.exception.UnAuthorizedException;
import com.project.nexstay.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.nexstay.utils.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomService roomService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating Hotel with name: {}",hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        log.info("Created a new Hotel with ID: {}",hotel.getId());
        return modelMapper.map(hotelRepository.save(hotel), HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id "+id+" not found!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+id);
        }
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating hotel with Id: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id "+id+" not found!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+id);
        }

        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        return modelMapper.map(hotelRepository.save(hotel),HotelDto.class);
    }

    @Override
    @Transactional
    public void activateHotelById(Long id) {
        log.info("Attempting to activate hotel with Id: {}",id);
        Hotel  hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id "+id+" not found!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+id);
        }

        hotel.setActive(true);
        hotelRepository.save(hotel);
        //assuming only do it once
        for(Room room :hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    @Transactional   // use when we are calling two database call from two tables either it does both operation or will not do anything
    public void deleteHotelById(Long id) {
        log.info("Attempting to delete hotel with Id: {}",id);
        Hotel hotel=hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel with id "+id+" not found!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+id);
        }

        for(Room room: hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomService.deleteRoomById(room.getId());
        }
        hotelRepository.deleteById(id);
    }

    //public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel =hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel with id "+hotelId+" not found!"));
        List<RoomDto> roomDtoList=hotel.getRooms()
                .stream()
                .map((entity)->modelMapper.map(entity, RoomDto.class))
                .collect(Collectors.toList());
        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),roomDtoList);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user=getCurrentUser();
        log.info("Getting all hotels for the admin user with id: {}",user.getId());
        return hotelRepository.findByOwner(user).stream()
                .map(hotel->modelMapper.map(hotel, HotelDto.class))
                .collect(Collectors.toList());
    }

}