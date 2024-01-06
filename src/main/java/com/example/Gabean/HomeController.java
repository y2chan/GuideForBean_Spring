package com.example.Gabean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private WeatherService weatherService; // 추가적으로 외부 API를 호출하는 로직을 처리하는 WeatherService 클래스를 만들어서 사용

    @RequestMapping("/")
    public String home(Model model) {
        String city = "서울특별시 노원구 화랑로 815";
        try {
            Map<String, Object> weatherInfo = weatherService.getWeather();
            model.addAttribute("weather_info", weatherInfo);
            model.addAttribute("city", city);
        } catch (HttpClientErrorException e) {
            // 타임아웃이 발생한 경우, 대체 동작 수행
            model.addAttribute("weather_info", Map.of(
                    "today_date", "N/A",
                    "current_time", "N/A",
                    "temperature", "N/A",
                    "weather_status", "N/A",
                    "wind_speed", "N/A",
                    "precipitation_type", "서버 응답 시간 초과"
            ));
            model.addAttribute("city", city);
        }
        return "home";
    }
}
