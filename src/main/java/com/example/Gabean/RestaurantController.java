package com.example.Gabean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RestaurantController {
    private final HumunFoodRepository humunFoodRepository;

    @Autowired
    public RestaurantController(HumunFoodRepository humunFoodRepository) {
        this.humunFoodRepository = humunFoodRepository;
    }

    @GetMapping("/get_all_restaurant")
    public ResponseEntity<?> getAllRestaurant() {
        // '카페'가 아닌 모든 고유한 'type'을 가져옵니다.
        List<String> types = humunFoodRepository.findAll()
                .stream()
                .filter(food -> !food.getType().equals("카페"))
                .map(HumunFood::getType)
                .distinct()
                .collect(Collectors.toList());

        // '카페'가 아닌 모든 고유한 'name'을 가져옵니다.
        List<String> items = humunFoodRepository.findAll()
                .stream()
                .filter(food -> !food.getType().equals("카페"))
                .map(HumunFood::getName)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(Map.of("types", types, "items", items));
    }
}
