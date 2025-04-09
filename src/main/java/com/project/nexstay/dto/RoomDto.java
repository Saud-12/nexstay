package com.project.nexstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private String type;
    private BigDecimal basePrice;
    private List<String> amenities;
    private List<String> photos;
    private Integer totalCount;
    private Integer capacity;
}