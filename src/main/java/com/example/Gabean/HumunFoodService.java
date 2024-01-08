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
            boolean isOpen = false; // 영업 상태 플래그

            for (OpeningHours openingHour : openingHoursList) {
                DayOfWeek openingDay = convertKoreanDayToDayOfWeek(openingHour.getWeek());
                if (openingDay != null && (openingDay == currentWeekday || openingHour.getWeek().equals("매일"))) {
                    if (currentTime.isAfter(openingHour.getOpenTime()) && currentTime.isBefore(openingHour.getCloseTime())) {
                        // 현재 시간이 오픈 시간과 클로즈 시간 사이라면 영업 중
                        restaurant.setOpenStatus("영업 중");
                        Duration duration = Duration.between(currentTime, openingHour.getCloseTime());
                        restaurant.setRemainingTime(formatDuration(duration));
                        isOpen = true;
                        break; // 영업 중이므로 더 이상 확인하지 않음
                    } else if (currentTime.isBefore(openingHour.getOpenTime())) {
                        // 현재 시간이 오픈 시간 전이라면 영업 전
                        restaurant.setOpenStatus("영업 전");
                        Duration duration = Duration.between(currentTime, openingHour.getOpenTime());
                        restaurant.setRemainingTime(formatDuration(duration));
                        isOpen = true;
                        break; // 영업 전 상태이므로 더 이상 확인하지 않음
                    }
                }
            }

            if (!isOpen) {
                // 영업 중이거나 영업 전 상태가 아닌 경우에만 마감 처리
                restaurant.setOpenStatus("마감");
                // 다음 영업 시작 시간까지 남은 시간을 계산
                LocalDateTime nextOpenDateTime = findNextOpeningTime(openingHoursList, currentDateTime);
                if (nextOpenDateTime != null) {
                    Duration durationUntilNextOpen = Duration.between(currentDateTime, nextOpenDateTime);
                    restaurant.setRemainingTime(formatDuration(durationUntilNextOpen));
                } else {
                    restaurant.setRemainingTime("영업 정보 없음");
                }
            }
        }
    }

    private LocalDateTime findNextOpeningTime(List<OpeningHours> openingHoursList, LocalDateTime currentDateTime) {
        // 다음 영업일의 오픈 시간을 찾는 로직을 구현합니다.
        // 이 예제에서는 단순화를 위해 현재 날짜의 다음 날을 가정하고 있습니다.
        // 실제 로직은 영업일을 고려하여 구현해야 합니다.
        LocalDate nextDay = currentDateTime.toLocalDate().plusDays(1);
        for (OpeningHours openingHour : openingHoursList) {
            if (openingHour.getWeek().equals("매일") || convertKoreanDayToDayOfWeek(openingHour.getWeek()) == nextDay.getDayOfWeek()) {
                return LocalDateTime.of(nextDay, openingHour.getOpenTime());
            }
        }
        return null;
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
