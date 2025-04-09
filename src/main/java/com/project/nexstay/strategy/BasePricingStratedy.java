package com.project.nexstay.strategy;

import com.project.nexstay.entity.Inventory;

import java.math.BigDecimal;

public class BasePricingStratedy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
