package com.project.nexstay.service;

import com.project.nexstay.dto.RoomDto;
import com.project.nexstay.entity.Hotel;
import com.project.nexstay.entity.Room;
import com.project.nexstay.entity.User;
import com.project.nexstay.exception.ResourceNotFoundException;
import com.project.nexstay.exception.UnAuthorizedException;
import com.project.nexstay.repository.HotelRepository;
import com.project.nexstay.repository.RoomRepository;
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
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    @Override
    public RoomDto createNewRoomByHotel(RoomDto roomDto,Long hotelId) {
        log.info("Attempting to create a new Room");
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel with ID: "+hotelId+" does not exists!"));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+hotelId);
        }

        Room room=modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room=roomRepository.save(room);
        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(room,RoomDto.class);


    }

    @Override
    public List<RoomDto> getAllRoomInHotel(Long hotelId) {
        log.info("Getting all rooms of Hotel with ID: {}",hotelId);
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel with ID: "+hotelId+" does not exists!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map(room->modelMapper.map(room,RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long id) {
        log.info("Attempting to fetch Room by ID: {}",id);
        Room room=roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Room with ID: "+id+" does not exists!"));
        return modelMapper.map(room,RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long id) {
        log.info("Attempting to delete room By ID: {} ",id);
        Room room=roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Room with ID: "+id+" does not exists!"));

        User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorizedException("This user does not own this hotel with id "+id);
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating the room with id:{} ",roomId);
        Hotel hotel=hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel with ID: "+hotelId+" does not exists!"));
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room with id: "+roomId+" does not exists!"));

        User user=getCurrentUser();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("Not authorized to perform this operation!");
        }

        modelMapper.map(roomDto,room);
        room.setId(roomId);

        //TODO: if price or inventory is updated, then update the inventory for this room
        room=roomRepository.save(room);
        return modelMapper.map(room,RoomDto.class);
    }
}
