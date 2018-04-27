package com.car.game.common.repository;

import com.car.game.common.enums.CarType;
import com.car.game.common.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Car findCarByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Car SET mapName = :mapName, type = :carType WHERE name = :name")
    void update(@Param("name") String name,
                @Param("mapName") String mapName,
                @Param("carType") CarType carType);

}
