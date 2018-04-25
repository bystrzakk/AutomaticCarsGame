package com.car.game.common.repository;

import com.car.game.common.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Car findCarByName(String name);

}
