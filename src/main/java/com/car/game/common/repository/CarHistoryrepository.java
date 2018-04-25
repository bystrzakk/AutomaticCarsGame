package com.car.game.common.repository;

import com.car.game.common.model.CarHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarHistoryrepository extends JpaRepository <CarHistory, Long> {

    @Query("Select ch.move from CarHistory ch where ch.car.carPk.name = :carName ORDER BY ch.id ASC")
    List<String> findAllCarMovements(@Param("carName")String carName);
}