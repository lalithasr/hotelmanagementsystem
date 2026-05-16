package com.example.hotelmanagementsystem.controller;

import com.example.hotelmanagementsystem.dto.ApiResponse;
import com.example.hotelmanagementsystem.dto.BookingRequestDto;
import com.example.hotelmanagementsystem.entity.Booking;
import com.example.hotelmanagementsystem.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Booking Management", description = "APIs for managing bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a booking")
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @Valid @RequestBody BookingRequestDto dto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        Booking booking = bookingService.createBooking(userEmail, dto);
        return new ResponseEntity<>(ApiResponse.created("Booking created successfully", booking), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all bookings (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.noContent("No bookings found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Bookings fetched successfully", bookings));
    }

    @GetMapping("/my-bookings")
    @Operation(summary = "Get logged-in user's bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings(Authentication authentication) {
        String userEmail = authentication.getName();
        List<Booking> bookings = bookingService.getBookingsByUser(userEmail);
        return ResponseEntity.ok(ApiResponse.success("Your bookings fetched successfully", bookings));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success("Booking fetched successfully", booking));
    }

    @PutMapping("/id/{id}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }
}

