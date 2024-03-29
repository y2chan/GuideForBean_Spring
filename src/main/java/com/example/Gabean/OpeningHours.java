package com.example.Gabean;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "GaBean_openinghours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String week;
    private LocalTime open_Time;
    private LocalTime close_Time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private HumunFood restaurant;

    // 표준 생성자
    public OpeningHours() {}

    // Getter와 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public LocalTime getOpenTime() {
        return open_Time;
    }

    public void setOpenTime(LocalTime openTime) {
        this.open_Time = openTime;
    }

    public LocalTime getCloseTime() {
        return close_Time;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.close_Time = closeTime;
    }

    public HumunFood getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(HumunFood restaurant) {
        this.restaurant = restaurant;
    }
}

