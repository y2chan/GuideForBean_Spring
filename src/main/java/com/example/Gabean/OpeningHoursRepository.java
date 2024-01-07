package com.example.Gabean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {
    // restaurantId 필드에 대한 쿼리 메소드
    List<OpeningHours> findByRestaurantId(Long restaurantId);
}