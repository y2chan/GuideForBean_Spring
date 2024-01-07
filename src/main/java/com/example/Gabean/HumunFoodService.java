package com.example.Gabean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;



@Service
public class HumunFoodService {
    private final HumunFoodRepository humunFoodRepository;
    private final OpeningHoursRepository openingHoursRepository;

    // 생성자 주입을 사용합니다. (권장되는 방법)
    @Autowired
    public HumunFoodService(HumunFoodRepository humunFoodRepository, OpeningHoursRepository openingHoursRepository) {
        this.humunFoodRepository = humunFoodRepository;
        this.openingHoursRepository = openingHoursRepository;
    }

    public List<HumunFood> getAllHumunFoods() {
        return humunFoodRepository.findAll();
    }

    // 한글 요일명을 DayOfWeek으로 변환하는 메소드
    private DayOfWeek convertKoreanDayToDayOfWeek(String koreanDay) {
        return DAY_MAPPING.getOrDefault(koreanDay, null);
    }

    // 영업 상태 및 남은 시간을 처리하는 로직
    public void processRestaurantOpenStatus(List<HumunFood> restaurants, LocalDateTime currentDateTime) {
        DayOfWeek currentWeekday = currentDateTime.getDayOfWeek();
        LocalTime currentTime = currentDateTime.toLocalTime();

        for (HumunFood restaurant : restaurants) {
            List<OpeningHours> openingHoursList = openingHoursRepository.findByRestaurantId(restaurant.getId());
            restaurant.setOpenStatus("휴무");  // 기본적으로 휴무로 설정
            restaurant.setRemainingTime("닫힘");  // 기본적으로 닫힘으로 설정

            for (OpeningHours openingHour : openingHoursList) {
                DayOfWeek openingDay = convertKoreanDayToDayOfWeek(openingHour.getWeek());
                if (openingDay != null && (openingDay == currentWeekday || openingHour.getWeek().equals("매일"))) {
                    if (currentTime.isAfter(openingHour.getOpenTime()) && currentTime.isBefore(openingHour.getCloseTime())) {
                        restaurant.setOpenStatus("영업 중");
                        Duration duration = Duration.between(currentTime, openingHour.getCloseTime());
                        restaurant.setRemainingTime(formatDuration(duration));
                        break;
                    }
                }
            }
        }
    }

    // 남은 시간을 "hh시간 mm분" 형식의 문자열로 포맷팅하는 메소드
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        return String.format("%d시간 %d분", hours, minutes);
    }

    // 모든 식당 유형을 조회하는 메소드
    public List<String> getRestaurantTypes() {
        return humunFoodRepository.findDistinctTypes();
    }

    // 요일 매핑
    private static final Map<String, DayOfWeek> DAY_MAPPING = new HashMap<>();
    static {
        DAY_MAPPING.put("월요일", DayOfWeek.MONDAY);
        DAY_MAPPING.put("화요일", DayOfWeek.TUESDAY);
        DAY_MAPPING.put("수요일", DayOfWeek.WEDNESDAY);
        DAY_MAPPING.put("목요일", DayOfWeek.THURSDAY);
        DAY_MAPPING.put("금요일", DayOfWeek.FRIDAY);
        DAY_MAPPING.put("토요일", DayOfWeek.SATURDAY);
        DAY_MAPPING.put("일요일", DayOfWeek.SUNDAY);
        // "매일"과 "휴무"는 여기서 처리하지 않습니다.
    }
}
