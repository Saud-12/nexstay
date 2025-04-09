package com.project.nexstay.controller;

import com.project.nexstay.dto.BookingDto;
import com.project.nexstay.dto.ProfileUpdateRequestDto;
import com.project.nexstay.dto.UserDto;
import com.project.nexstay.service.BookingService;
import com.project.nexstay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto){
        userService.updateUserProfile(profileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>> getMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile(){
        return ResponseEntity.ok(userService.getMyProfile());
    }
}
