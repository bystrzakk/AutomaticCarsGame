package com.car.game.map;

import com.car.game.common.repository.MapRepository;
import com.car.game.configuration.TestConfig;
import com.car.game.map.service.MapService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


public class MapControllerTest extends TestConfig{

    @Autowired
    private MapService mapService;

    @Autowired
    private MapRepository mapRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.mapRepository.deleteAllInBatch();
        //this.mapService.addNewMap();
    }

    @Test
    public void testAddCar() throws Exception{

    }






}