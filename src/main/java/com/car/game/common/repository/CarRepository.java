package com.car.game.common.repository;

import com.car.game.common.model.Car;
import com.car.game.common.model.CarPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, CarPk> {

    Car findCarByCarPk(CarPk carPk);

}
