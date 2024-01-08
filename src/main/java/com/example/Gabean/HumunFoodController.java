package com.example.Gabean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class HumunFoodController {
    private final HumunFoodService humunFoodService;

    @Autowired
    public HumunFoodController(HumunFoodService humunFoodService) {
        this.humunFoodService = humunFoodService;
    }

    @GetMapping(value = {"/humun_food", "/m/m_humun_food"})
    public String getRestaurants(Model model, HttpServletRequest request) {
        // 식당 목록과 식당 유형을 가져옵니다.
        List<HumunFood> restaurants = humunFoodService.getAllHumunFoods();
        List<String> restaurantTypes = humunFoodService.getRestaurantTypes();

        // 현재 시간을 가져옵니다.
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 시간을 "2:15 PM" 형식으로 포맷합니다.
        String formattedCurrentTime = currentDateTime.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH));
        // 문자열을 "2:15 p.m." 형식으로 변경합니다.
        formattedCurrentTime = formattedCurrentTime
                .toLowerCase() // 소문자로 변경합니다.
                .replace("am", "a.m.") // "AM"을 "a.m."으로 변경합니다.
                .replace("pm", "p.m."); // "PM"을 "p.m."으로 변경합니다.

        // 영업 상태와 남은 시간을 처리합니다.
        humunFoodService.processRestaurantOpenStatus(restaurants, currentDateTime);

        // 모델에 데이터를 추가합니다.
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("restaurantTypes", restaurantTypes);
        model.addAttribute("currentDateTime", formattedCurrentTime);

        // 뷰 선택 로직
        String viewName = "humun_food";
        String requestURL = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();

        if (requestURL.startsWith("/m/")) {
            viewName = "mobile/m_humun_food";
        }

        return viewName;
    }
}

