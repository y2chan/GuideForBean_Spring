package com.example.Gabean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
@RequestMapping("/api")
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/weather")
    public Map<String, Object> getWeather() {
        Map<String, Object> weatherInfo = new HashMap<>();
        try {
            logger.info("API 키 사용 중: {}", apiKey); // API 키를 로깅

            String baseDateTime = getBaseDateTime();

            // RestTemplate을 생성할 때 DefaultUriBuilderFactory를 사용하여 인코딩을 변경
            DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
            uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setUriTemplateHandler(uriBuilderFactory);

            // UriComponentsBuilder를 사용하여 올바른 인코딩으로 URL을 구성
            String url = UriComponentsBuilder.fromUriString("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                    .queryParam("serviceKey", apiKey)
                    .queryParam("numOfRows", 10)
                    .queryParam("pageNo", 1)
                    .queryParam("base_date", baseDateTime.split(" ")[0])
                    .queryParam("base_time", baseDateTime.split(" ")[1])
                    .queryParam("nx", 62)
                    .queryParam("ny", 128)
                    .queryParam("dataType", "JSON")
                    .build().toUriString();

            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (!root.hasNonNull("response") || !root.path("response").hasNonNull("body") || !root.path("response").path("body").hasNonNull("items")) {
                throw new Exception("잘못된 API 응답 형식");
            }

            JsonNode items = root.path("response").path("body").path("items").get("item");
            processWeatherData(items, weatherInfo);
        } catch (Exception e) {
            System.out.println("API 응답 처리 중 오류: " + e.getMessage());
            weatherInfo.put("today_date", "N/A");
            weatherInfo.put("current_time", "N/A");
            weatherInfo.put("temperature", "N/A");
            weatherInfo.put("weather_status", "N/A");
            weatherInfo.put("wind_speed", "N/A");
            weatherInfo.put("precipitation_type", "서버 응답 시간 초과");
        }

        return weatherInfo;
    }

    private void processWeatherData(JsonNode items, Map<String, Object> weatherInfo) {
        items.forEach(item -> {
            String category = item.get("category").asText();
            String fcstValue = item.get("fcstValue").asText();

            switch (category) {
                case "TMP":
                    weatherInfo.put("temperature", fcstValue);
                    break;
                case "SKY":
                    int skyValue = Integer.parseInt(fcstValue);
                    if (skyValue >= 0 && skyValue <= 5) {
                        weatherInfo.put("weather_status", "구름 없음");
                    } else if (skyValue >= 6 && skyValue <= 8) {
                        weatherInfo.put("weather_status", "구름 많음");
                    } else if (skyValue >= 9 && skyValue <= 10) {
                        weatherInfo.put("weather_status", "흐림");
                    }
                    break;
                case "WSD":
                    float windSpeedValue = Float.parseFloat(fcstValue);
                    if (windSpeedValue >= 0 && windSpeedValue < 4) {
                        weatherInfo.put("wind_speed", "선선함");
                    } else if (windSpeedValue >= 4 && windSpeedValue < 9) {
                        weatherInfo.put("wind_speed", "약풍");
                    } else if (windSpeedValue >= 9 && windSpeedValue < 14) {
                        weatherInfo.put("wind_speed", "강풍");
                    } else if (windSpeedValue >= 14) {
                        weatherInfo.put("wind_speed", "심한 강풍");
                    }
                    break;
                case "PTY":
                    int ptyValue = Integer.parseInt(fcstValue);
                    if (ptyValue == 0) {
                        weatherInfo.put("precipitation_type", "맑음");
                    } else if (ptyValue == 1) {
                        weatherInfo.put("precipitation_type", "비");
                    } else if (ptyValue == 2) {
                        weatherInfo.put("precipitation_type", "비/눈");
                    } else if (ptyValue == 3) {
                        weatherInfo.put("precipitation_type", "눈");
                    } else if (ptyValue == 4) {
                        weatherInfo.put("precipitation_type", "소나기");
                    }
                    break;
            }
        });

        weatherInfo.put("today_date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        weatherInfo.put("current_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private String getBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int currentHour = now.getHour();
        String baseTime;
        String baseDate;

        if (currentHour < 2.5) {
            baseTime = "2300";
            baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 5.5) {
            baseTime = "0200";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 8.5) {
            baseTime = "0500";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 11.5) {
            baseTime = "0800";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 14.5) {
            baseTime = "1100";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 17.5) {
            baseTime = "1400";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 20.5) {
            baseTime = "1700";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else if (currentHour < 23.5) {
            baseTime = "2000";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else {
            baseTime = "2300";
            baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        return baseDate + " " + baseTime;
    }
}
