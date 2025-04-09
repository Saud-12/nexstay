package com.project.nexstay.strategy;

import com.project.nexstay.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        double occupancyRate=inventory.getBookedCount()/ inventory.getTotalCount();
        return occupancyRate>0.8?price.multiply(BigDecimal.valueOf(1.2)):price;
    }
}
