package com.example.Gabean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HumunFoodRepository extends JpaRepository<HumunFood, Long> {
    @Query("SELECT DISTINCT type FROM HumunFood")
    List<String> findDistinctTypes();
}
