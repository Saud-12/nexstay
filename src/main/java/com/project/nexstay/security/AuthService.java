package com.project.nexstay.security;

import com.project.nexstay.dto.LoginDto;
import com.project.nexstay.dto.SignupRequestDto;
import com.project.nexstay.dto.UserDto;
import com.project.nexstay.entity.User;
import com.project.nexstay.enums.Role;
import com.project.nexstay.exception.ResourceNotFoundException;
import com.project.nexstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDto signup(SignupRequestDto signupRequestDto){
        User user=userRepository.findByEmail(signupRequestDto.getEmail());
        if(user!=null){
            throw new RuntimeException("User with this email already exists");
        }
        User newUser=modelMapper.map(signupRequestDto,User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        newUser=userRepository.save(newUser);

        return modelMapper.map(newUser,UserDto.class);
    }

    public String[] login(LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        User user=(User)authentication.getPrincipal();
        String accessToken=jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);
        String[] arr={accessToken,refreshToken};
        return arr;
    }

    public String refreshToken(String refreshToken){
        Long id=jwtService.getUserIdFromToken(refreshToken);
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id: "+id+" does not exists!"));
        return jwtService.generateAccessToken(user);
    }
}
