package com.car.game.cars;

import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarService;
import com.car.game.common.enums.CarType;

import com.car.game.common.enums.Move;
import com.car.game.common.repository.CarHistoryrepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.configuration.TestConfig;


import com.car.game.game.FieldPosition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;



public class CarControllerTest extends TestConfig {

    @Autowired
    private CarService carsService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarHistoryrepository carHistoryrepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.carHistoryrepository.deleteAllInBatch();
        this.carRepository.deleteAllInBatch();
    }

    @Test
    public void testAddCar() throws Exception{
        mockMvc.perform(post("/car")
                .contentType(contentType)
                .content(json(getCarDto())))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCars() throws Exception {
        carsService.addCar(getCarDto());
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetCarHistory() throws Exception {
        mockMvc.perform(get("/car-history").param("carName", "BMW"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveCar() throws Exception {
        mockMvc.perform(delete("/car")
                .contentType(contentType)
                .content(json(getCarDto())))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveCarFromGame() throws Exception {
        mockMvc.perform(delete("/car-remove-from-game").param("carName","testName"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPutCarInMap() throws Exception {
        mockMvc.perform(post("/car-first-setup")
                .contentType(contentType)
                .content(json(getCarSetup())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCarRepair() throws Exception {
        mockMvc.perform(post("/car-repair").param("carName","testName"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testMoveCarOnMap() throws Exception {
        mockMvc.perform(post("/car-move")
                .contentType(contentType)
                .content(json(getCarMove())))
                .andDo(print())
                .andExpect(status().isOk());
    }


    private CarInformation getCarDto(){
        return new CarInformation("testCarName", CarType.NORMAL);
    }

    private CarSetup getCarSetup(){
        return new CarSetup(getCarDto(),"testMapName", new FieldPosition(0,0));
    }

    private CarMove getCarMove(){
        return new CarMove("testName",CarType.NORMAL,Move.FORWARD,"testMapName");
    }

    private Move getMove(){
        return Move.TURN_LEFT;
    }


}