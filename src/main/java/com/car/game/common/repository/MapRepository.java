package com.car.game.common.repository;

import com.car.game.common.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Map, Long> {

    Map findByNameAndUsedIsFalse(String name);
}
