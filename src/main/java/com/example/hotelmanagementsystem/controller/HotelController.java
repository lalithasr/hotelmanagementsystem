package com.example.hotelmanagementsystem.controller;

import com.example.hotelmanagementsystem.dto.ApiResponse;
import com.example.hotelmanagementsystem.dto.HotelRequestDto;
import com.example.hotelmanagementsystem.entity.Hotel;
import com.example.hotelmanagementsystem.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Hotel Management", description = "APIs for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    @Operation(summary = "Get all hotels with pagination")
    public ResponseEntity<ApiResponse<Page<Hotel>>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Hotel> hotels = hotelService.getAllHotelsPaged(page, size);
        if (hotels.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.noContent("No hotels found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Hotels fetched successfully", hotels));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get hotel by ID")
    public ResponseEntity<ApiResponse<Hotel>> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(ApiResponse.success("Hotel fetched successfully", hotel));
    }

    @PostMapping
    @Operation(summary = "Create a new hotel (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Hotel>> createHotel(@Valid @RequestBody HotelRequestDto dto) {
        Hotel hotel = hotelService.saveHotel(dto);
        return new ResponseEntity<>(ApiResponse.created("Hotel created successfully", hotel), HttpStatus.CREATED);
    }

    @PutMapping("/id/{id}")
    @Operation(summary = "Update hotel by ID (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Hotel>> updateHotel(@PathVariable Long id,
                                                           @Valid @RequestBody HotelRequestDto dto) {
        Hotel hotel = hotelService.updateHotel(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Hotel updated successfully", hotel));
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Delete hotel by ID (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(ApiResponse.success("Hotel deleted successfully", null));
    }
}