package com.car.game.cars;

import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarsService;
import com.car.game.common.enums.CarType;

import com.car.game.common.enums.Move;
import com.car.game.common.repository.CarRepository;
import com.car.game.configuration.TestConfig;
import com.car.game.game.ActualInformation;

import com.car.game.game.FieldPosition;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;



public class CarsControllerTest extends TestConfig {

    @Autowired
    private CarsService carsService;

    @Autowired
    private CarRepository carRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.carRepository.deleteAllInBatch();
        //this.carsService.addCar(getCarDto());
    }

    @Test
    public void testAddCar() throws Exception{
        this.mockMvc.perform(post("/car")
                .contentType(contentType)
                .content(json(getCarDto())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCars() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testRemoveCar() throws Exception {
        mockMvc.perform(delete("/car")
                .contentType(contentType)
                .content(json(getCarDto())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPutCarInMap() throws Exception {
//        ActualInformation.setActualMapName("testMap");
//        ActualInformation.setConcuretnHashMapGame(getMap());

        mockMvc.perform(post("/car/first/setup")
                .contentType(contentType)
                .content(json(getCarSetup())))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testMoveCarOnMap() throws Exception {
//        ActualInformation.setActualMapName("testMap");
//        ActualInformation.setConcuretnHashMapGame(getMap());
//
//        mockMvc.perform(post("/car/move")
//                .contentType(contentType)
//                .content(json(getCarMove())))
//                .andExpect(status().isOk());
//    }

    private CarInformation getCarDto(){
        return new CarInformation("testCarName", CarType.NORMAL);
    }

    private CarSetup getCarSetup(){
        return new CarSetup(getCarDto(),"testMapName", new FieldPosition(0,0));
    }

    private CarMove getCarMove(){
        return new CarMove();
    }

    private Move getMove(){
        return Move.TURN_LEFT;
    }


}