package com.car.game.common.repository;

import com.car.game.common.model.MapGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<MapGame, Long> {

    MapGame findByNameAndUsedIsFalseAndDeletedIsFalse(String name);

    MapGame findByName(String name);
}
