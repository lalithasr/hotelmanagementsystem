package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.dto.RoomRequestDto;
import com.example.hotelmanagementsystem.entity.Hotel;
import com.example.hotelmanagementsystem.entity.Room;
import com.example.hotelmanagementsystem.exception.HotelNotFoundException;
import com.example.hotelmanagementsystem.exception.RoomNotFoundException;
import com.example.hotelmanagementsystem.repository.HotelRepository;
import com.example.hotelmanagementsystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public List<Room> getAllRooms() {
        log.info("RoomService::getAllRooms - Fetching all rooms");
        return roomRepository.findAll();
    }

    public List<Room> getRoomsByHotel(Long hotelId) {
        log.info("RoomService::getRoomsByHotel - Fetching rooms for hotel id: {}", hotelId);
        return roomRepository.findByHotelId(hotelId);
    }

    public List<Room> getAvailableRooms() {
        log.info("RoomService::getAvailableRooms - Fetching available rooms");
        return roomRepository.findByAvailableTrue();
    }

    public Room getRoomById(Long id) {
        log.info("RoomService::getRoomById - Fetching room id: {}", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with id: " + id));
    }

    public Room createRoom(RoomRequestDto dto) {
        log.info("RoomService::createRoom - Creating room: {}", dto.getRoomNumber());
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + dto.getHotelId()));

        Room room = Room.builder()
                .roomNumber(dto.getRoomNumber())
                .type(dto.getType())
                .price(dto.getPrice())
                .available(dto.getAvailable())
                .hotel(hotel)
                .build();
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, RoomRequestDto dto) {
        log.info("RoomService::updateRoom - Updating room id: {}", id);
        Room room = getRoomById(id);
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + dto.getHotelId()));

        room.setRoomNumber(dto.getRoomNumber());
        room.setType(dto.getType());
        room.setPrice(dto.getPrice());
        room.setAvailable(dto.getAvailable());
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        log.info("RoomService::deleteRoom - Deleting room id: {}", id);
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }
}

