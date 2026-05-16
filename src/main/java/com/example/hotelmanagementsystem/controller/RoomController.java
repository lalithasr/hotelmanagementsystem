package com.example.hotelmanagementsystem.controller;

import com.example.hotelmanagementsystem.dto.ApiResponse;
import com.example.hotelmanagementsystem.dto.RoomRequestDto;
import com.example.hotelmanagementsystem.entity.Room;
import com.example.hotelmanagementsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Room Management", description = "APIs for managing rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @Operation(summary = "Get all rooms")
    public ResponseEntity<ApiResponse<List<Room>>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.noContent("No rooms found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Rooms fetched successfully", rooms));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available rooms")
    public ResponseEntity<ApiResponse<List<Room>>> getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success("Available rooms fetched", rooms));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<ApiResponse<Room>> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.success("Room fetched successfully", room));
    }

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Get rooms by hotel ID")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.success("Rooms fetched for hotel", rooms));
    }

    @PostMapping
    @Operation(summary = "Create a new room (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Room>> createRoom(@Valid @RequestBody RoomRequestDto dto) {
        Room room = roomService.createRoom(dto);
        return new ResponseEntity<>(ApiResponse.created("Room created successfully", room), HttpStatus.CREATED);
    }

    @PutMapping("/id/{id}")
    @Operation(summary = "Update room by ID (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Room>> updateRoom(@PathVariable Long id,
                                                         @Valid @RequestBody RoomRequestDto dto) {
        Room room = roomService.updateRoom(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Room updated successfully", room));
    }

    @DeleteMapping("/id/{id}")
    @Operation(summary = "Delete room by ID (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room deleted successfully", null));
    }
}

