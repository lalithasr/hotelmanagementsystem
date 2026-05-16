package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.dto.BookingRequestDto;
import com.example.hotelmanagementsystem.entity.Booking;
import com.example.hotelmanagementsystem.entity.Room;
import com.example.hotelmanagementsystem.entity.User;
import com.example.hotelmanagementsystem.exception.BookingNotFoundException;
import com.example.hotelmanagementsystem.exception.RoomNotAvailableException;
import com.example.hotelmanagementsystem.exception.UserNotFoundException;
import com.example.hotelmanagementsystem.repository.BookingRepository;
import com.example.hotelmanagementsystem.repository.RoomRepository;
import com.example.hotelmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public Booking createBooking(String userEmail, BookingRequestDto dto) {
        log.info("BookingService::createBooking - Booking room {} by {}", dto.getRoomId(), userEmail);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotAvailableException("Room not found with id: " + dto.getRoomId()));

        if (Boolean.FALSE.equals(room.getAvailable())) {
            throw new RoomNotAvailableException("Room is not available: " + dto.getRoomId());
        }

        long nights = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        double totalPrice = nights * room.getPrice();

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .totalPrice(totalPrice)
                .status("CONFIRMED")
                .build();

        // Mark room as unavailable
        room.setAvailable(false);
        roomRepository.save(room);

        Booking saved = bookingRepository.save(booking);
        log.info("BookingService::createBooking - Booking created with id: {}", saved.getId());
        return saved;
    }

    public List<Booking> getAllBookings() {
        log.info("BookingService::getAllBookings");
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUser(String userEmail) {
        log.info("BookingService::getBookingsByUser - email: {}", userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        return bookingRepository.findByUserId(user.getId());
    }

    public Booking getBookingById(Long id) {
        log.info("BookingService::getBookingById - id: {}", id);
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }

    public Booking cancelBooking(Long id) {
        log.info("BookingService::cancelBooking - id: {}", id);
        Booking booking = getBookingById(id);
        booking.setStatus("CANCELLED");

        // Free the room
        booking.getRoom().setAvailable(true);
        roomRepository.save(booking.getRoom());

        return bookingRepository.save(booking);
    }
}

