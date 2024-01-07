package com.example.Gabean;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Controller
public class ShuttleInfoController {

    @GetMapping(value = {"/info_shuttle", "/m/m_info_shuttle"})
    public String getInfoShuttle(Model model) {
        // 현재 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now();

        // 현재 요일을 가져옵니다. (월요일: 1, 일요일: 7)
        DayOfWeek currentWeekday = now.getDayOfWeek();

        // LinkedHashMap을 사용하여 삽입 순서를 유지합니다.
        Map<String, String> leftInfo = new LinkedHashMap<>();
        Map<String, String> rightInfo = new LinkedHashMap<>();

        // 새로운 시간표가 적용되는 기간을 정의합니다.
        LocalDate specialPeriodStart = LocalDate.of(2023, 12, 15);
        LocalDate specialPeriodEnd = LocalDate.of(2024, 1, 16);

        // 방학 기간을 정의합니다.
        LocalDate vacationPeriodStart = LocalDate.of(2024, 1, 17);
        LocalDate vacationPeriodEnd = LocalDate.of(2024, 3, 1);

        // 현재 날짜가 새로운 시간표가 적용되는 기간과 방학 기간에 속하는지 확인합니다.
        boolean isSpecialPeriod = !now.toLocalDate().isBefore(specialPeriodStart) && !now.toLocalDate().isAfter(specialPeriodEnd);
        boolean isVacationPeriod = !now.toLocalDate().isBefore(vacationPeriodStart) && !now.toLocalDate().isAfter(vacationPeriodEnd);

        // 남은 시간을 계산합니다.
        Map<String, String> remainingTimes = new HashMap<>();

        Map<String, String[]> timetable;
        if (isSpecialPeriod) {  // 특정 기간 동안
            if (currentWeekday.getValue() >= 1 && currentWeekday.getValue() <= 4) {  // 월요일부터 목요일까지
                timetable = ShuttleTimetable.getSeasonalWeekdayTimetable();
            } else if (currentWeekday == DayOfWeek.FRIDAY) {  // 금요일
                timetable = ShuttleTimetable.getSeasonalFridayTimetable();
            } else {  // 토요일과 일요일
                timetable = ShuttleTimetable.getSeasonalWeekendTimetable();
            }
        } else {  // 특정 기간이 아닌 경우
            if (currentWeekday.getValue() >= 1 && currentWeekday.getValue() <= 4) {  // 월요일부터 목요일까지
                timetable = ShuttleTimetable.getWeekdayTimetable();
            } else if (currentWeekday == DayOfWeek.FRIDAY) {  // 금요일
                timetable = ShuttleTimetable.getFridayTimetable();
            } else {  // 토요일과 일요일
                timetable = ShuttleTimetable.getWeekendTimetable();
            }
        }

        // "화랑대 -> 학교" 정보를 먼저 처리합니다.
        String[] hwangrangToSchoolTimes = timetable.get("화랑대 -> 학교");
        if (hwangrangToSchoolTimes != null) {
            insertShuttleInfo(leftInfo, rightInfo, "화랑대 -> 학교", hwangrangToSchoolTimes, now, isSpecialPeriod, isVacationPeriod);
        }

        // 남은 시간을 계산하고 leftInfo 및 rightInfo를 채우는 로직
        for (Map.Entry<String, String[]> entry : timetable.entrySet()) {
            String shuttle = entry.getKey();
            // "화랑대 -> 학교"는 이미 처리했으므로 건너뜁니다.
            if ("화랑대 -> 학교".equals(shuttle)) {
                continue;
            }

            String[] times = entry.getValue();
            insertShuttleInfo(leftInfo, rightInfo, shuttle, times, now, isSpecialPeriod, isVacationPeriod);
        }

        model.addAttribute("leftInfo", leftInfo);
        model.addAttribute("rightInfo", rightInfo);
        model.addAttribute("isSpecialPeriod", isSpecialPeriod);
        model.addAttribute("isVacationPeriod", isVacationPeriod);


        // 뷰 선택 로직
        String viewName = "info_shuttle";
        String requestURL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_info_shuttle";
        }

        return viewName;
    }
    // 셔틀 정보를 삽입하는 별도의 메서드
    private void insertShuttleInfo(Map<String, String> leftInfo, Map<String, String> rightInfo, String shuttle, String[] times, LocalDateTime now, boolean isSpecialPeriod, boolean isVacationPeriod) {
        // 다음 출발 시간을 찾습니다.
        String nextDepartureTime = findNextDepartureTime(times, now);
        if (nextDepartureTime != null) {
            // 방학 기간이 아닐 경우에만 계산합니다.
            if (!isVacationPeriod) {
                String remainingTime = calculateRemainingTime(nextDepartureTime, now);
                int leftInfoLimit = isSpecialPeriod ? 1 : 3; // 특별 기간 동안은 1개로 제한
                if (leftInfo.size() < leftInfoLimit) {
                    leftInfo.put(shuttle, remainingTime);
                } else {
                    rightInfo.put(shuttle, remainingTime);
                }
            }
        } else {
            int leftInfoLimit = isSpecialPeriod ? 1 : 3; // 특별 기간 동안은 1개로 제한
            if (leftInfo.size() < leftInfoLimit) {
                leftInfo.put(shuttle, "운행 종료");
            } else {
                rightInfo.put(shuttle, "운행 종료");
            }
        }
    }

    private String findNextDepartureTime(String[] times, LocalDateTime now) {
        LocalTime nowTime = now.toLocalTime(); // 현재 시간만 추출

        for (String time : times) {
            LocalTime departureTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            if (departureTime.isAfter(nowTime)) {
                return departureTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        }
        return null; // 다음 출발 시간이 없으면 null을 반환합니다.
    }

    private String calculateRemainingTime(String nextDepartureTimeStr, LocalDateTime now) {
        LocalTime nextDepartureTime = LocalTime.parse(nextDepartureTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime nextDepartureDateTime = LocalDateTime.of(now.toLocalDate(), nextDepartureTime);

        if (now.compareTo(nextDepartureDateTime) > 0) {
            // 현재 시간이 다음 출발 시간을 지났다면, 다음 날짜로 설정합니다.
            nextDepartureDateTime = nextDepartureDateTime.plusDays(1);
        }

        long minutesUntilDeparture = Duration.between(now, nextDepartureDateTime).toMinutes();
        return String.format("%d분 남았습니다. (%s에 출발)", minutesUntilDeparture, nextDepartureTimeStr);
    }

}
