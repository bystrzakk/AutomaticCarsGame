package com.car.game.common.repository;

import com.car.game.common.enums.Move;
import com.car.game.common.model.CarHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarHistoryRepository extends JpaRepository <CarHistory, Long> {

    @Query("Select ch.move from CarHistory ch where ch.car.name = :carName ORDER BY ch.id ASC")
    List<Move> findAllCarMovements(@Param("carName")String carName);
}