package com.project.nexstay.service;

import com.project.nexstay.dto.HotelPriceDto;
import com.project.nexstay.dto.HotelSearchRequest;
import com.project.nexstay.dto.InventoryDto;
import com.project.nexstay.dto.UpdateInventoryRequestDto;
import com.project.nexstay.entity.Inventory;
import com.project.nexstay.entity.Room;
import com.project.nexstay.entity.User;
import com.project.nexstay.exception.ResourceNotFoundException;
import com.project.nexstay.repository.HotelMinPriceRepository;
import com.project.nexstay.repository.InventoryRepository;
import com.project.nexstay.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.nexstay.utils.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today= LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for(;!today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory=Inventory.builder()
                    .room(room)
                    .hotel(room.getHotel())
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .price(room.getBasePrice())
                    .date(today)
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting the inventories of room with ID: "+room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels in {} city,from {} to {}",hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        Pageable pageable= PageRequest.of(hotelSearchRequest.getPage(),hotelSearchRequest.getSize());
        long dateCount= ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;
        Page<HotelPriceDto> hotelPage=hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),dateCount,pageable);
        return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Fetching all the inventories of room with id: {}",roomId);

       Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room with id: "+roomId+" does not exists"));
       User user=getCurrentUser();
       User hotelOwner=room.getHotel().getOwner();

       if(!user.equals(hotelOwner)){
           throw new AccessDeniedException("Not Authorized to perform this operation!");
       }
       return inventoryRepository.findByRoomOrderByDate(room).stream()
               .map(inventory -> modelMapper.map(inventory,InventoryDto.class))
               .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventoryByRoom(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating the inventory for the room with id: {} between the date range {} - {}",roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room with id: "+roomId+" does not exists!"));

        User user=getCurrentUser();
        User hotelOwner=room.getHotel().getOwner();

        if(!user.equals(hotelOwner)){
            throw new AccessDeniedException("Not authorized to perform this operation!");
        }
        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());
        inventoryRepository.updateInventory(roomId,updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(),updateInventoryRequestDto.getClosed(),updateInventoryRequestDto.getSurgeFactor());
    }
}
