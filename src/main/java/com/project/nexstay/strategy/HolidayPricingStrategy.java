package com.project.nexstay.strategy;

import com.project.nexstay.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        boolean isTodayHoliday=true;    //TODO: call an API or check with local data
        if(isTodayHoliday){
            return price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
