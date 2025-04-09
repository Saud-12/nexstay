package com.project.nexstay.dto;

import com.project.nexstay.entity.HotelContactInfo;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String city;
    //    @Column(columnDefinition = "JSON")
    private List<String> photos;
    //    @Column(columnDefinition = "JSON")
    private List<String> amenities;
    private Boolean active;
    private HotelContactInfo contactInfo;
}