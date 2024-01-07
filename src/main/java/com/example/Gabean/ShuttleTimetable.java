package com.example.Gabean;

import java.util.HashMap;
import java.util.Map;

public class ShuttleTimetable {

    public static Map<String, String[]> getWeekdayTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("화랑대 -> 학교", new String[]{"08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55", "10:00", "10:20", "10:40", "11:00", "11:20", "11:40", "12:00"});
        timetable.put("태릉입구, 석계 -> 학교", new String[]{"12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00", "15:20", "15:40", "16:00", "16:20", "16:40", "17:00", "17:20", "17:40", "18:00", "18:15"});
        timetable.put("별내 -> 학교", new String[]{"08:40", "09:40", "10:40", "11:40", "12:40", "13:40", "14:40", "15:40", "16:40", "17:40"});
        timetable.put("학교 -> 태릉입구, 석계", new String[]{"12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00", "15:20", "15:40", "16:00", "16:20", "16:40", "17:00", "17:20", "17:40", "18:00", "18:15"});
        timetable.put("학교 -> 별내", new String[]{"10:25", "11:25", "12:25", "13:25", "14:25", "15:25", "16:25", "17:25"});
        // 나머지 노선 시간표를 추가할 수 있습니다.
        return timetable;
    }

    public static Map<String, String[]> getFridayTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("화랑대 -> 학교", new String[]{"08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:50", "09:55", "10:00", "10:20", "10:40", "11:00", "11:20", "11:40", "12:00"});
        timetable.put("태릉입구, 석계 -> 학교", new String[]{"12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00", "15:20", "15:30"});
        timetable.put("별내 -> 학교", new String[]{"08:40", "09:40", "10:40", "11:40", "12:40", "13:40", "14:40", "15:40"});
        timetable.put("학교 -> 태릉입구, 석계", new String[]{"12:00", "12:25", "12:50", "13:15", "13:40", "14:05", "14:30", "15:00", "15:20", "15:40"});
        timetable.put("학교 -> 별내", new String[]{"10:25", "11:25", "12:25", "13:25", "14:25", "15:25"});
        // 나머지 노선 시간표를 추가할 수 있습니다.
        return timetable;
    }

    public static Map<String, String[]> getWeekendTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("전체 노선", new String[]{"운행 종료"});
        return timetable;
    }

    public static Map<String, String[]> getSeasonalWeekdayTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("화랑대 -> 학교", new String[]{"08:00", "08:10", "09:10", "10:10", "11:10", "12:10", "13:10", "14:10", "15:10", "16:10", "17:10"});
        timetable.put("학교 -> 화랑대", new String[]{"08:30", "08:40", "09:30" , "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30"});
        // 나머지 노선 시간표를 추가할 수 있습니다.
        return timetable;
    }

    public static Map<String, String[]> getSeasonalFridayTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("화랑대 -> 학교", new String[]{"08:00", "08:10", "09:10", "10:10", "11:10", "12:10", "13:10", "14:10"});
        timetable.put("학교 -> 화랑대", new String[]{"08:30", "08:40", "09:30" , "10:30", "11:30", "12:30", "13:30", "14:30"});
        // 나머지 노선 시간표를 추가할 수 있습니다.
        return timetable;
    }

    public static Map<String, String[]> getSeasonalWeekendTimetable() {
        Map<String, String[]> timetable = new HashMap<>();
        timetable.put("전체 노선", new String[]{"운행 종료"});
        return timetable;
    }
}

