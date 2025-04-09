package com.project.nexstay.dto;

import com.project.nexstay.entity.User;
import com.project.nexstay.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto {
    private Long id;

    private User user;

    private String name;

    private Integer age;

    private Gender gender;
}
