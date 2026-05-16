package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.dto.HotelRequestDto;
import com.example.hotelmanagementsystem.entity.Hotel;
import com.example.hotelmanagementsystem.exception.HotelNotFoundException;
import com.example.hotelmanagementsystem.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        log.info("HotelService::getAllHotels - Fetching all hotels");
        return hotelRepository.findAll();
    }

    public Page<Hotel> getAllHotelsPaged(int page, int size) {
        log.info("HotelService::getAllHotelsPaged - page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepository.findAll(pageable);
    }

    public Hotel getHotelById(Long id) {
        log.info("HotelService::getHotelById - Fetching hotel with id: {}", id);
        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
    }

    public Hotel saveHotel(HotelRequestDto dto) {
        log.info("HotelService::saveHotel - Creating hotel: {}", dto.getName());
        Hotel hotel = Hotel.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .contactNumber(dto.getContactNumber())
                .rating(dto.getRating())
                .build();
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, HotelRequestDto dto) {
        log.info("HotelService::updateHotel - Updating hotel id: {}", id);
        Hotel hotel = getHotelById(id);
        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setAddress(dto.getAddress());
        hotel.setDescription(dto.getDescription());
        hotel.setContactNumber(dto.getContactNumber());
        hotel.setRating(dto.getRating());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        log.info("HotelService::deleteHotel - Deleting hotel id: {}", id);
        Hotel hotel = getHotelById(id);
        hotelRepository.delete(hotel);
    }
}