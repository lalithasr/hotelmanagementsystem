package com.example.hotelmanagementsystem.repository;

import com.example.hotelmanagementsystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    List<Room> findByAvailableTrue();

    List<Room> findByHotelIdAndAvailableTrue(Long hotelId);
}