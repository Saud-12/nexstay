package com.project.nexstay.repository;

import com.project.nexstay.entity.Booking;
import com.project.nexstay.entity.Hotel;
import com.project.nexstay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByPaymentSessionId(String sessionId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.guests WHERE b.hotel = :hotel")
    List<Booking> findByHotel(@Param("hotel") Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Booking> findByUser(User user);
}
