package com.project.nexstay.service;

import com.project.nexstay.entity.Booking;

public interface CheckoutService {

   String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
