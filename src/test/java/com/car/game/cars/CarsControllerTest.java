package com.car.game.cars;

import com.car.game.Application;
import com.car.game.cars.dto.CarDto;
import com.car.game.cars.dto.CarMoveDto;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarsService;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.CarPk;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.ActualInformation;
import com.car.game.game.MapInformation;
import com.car.game.game.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.car.game.common.enums.Direction.N;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CarsControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarsService carsService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.carRepository.deleteAllInBatch();
        this.carsService.addCar(getCarDto());
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
        ActualInformation.setActualMapName("testMap");
        ActualInformation.setConcuretnHashMapGame(getMap());

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

    private ConcurrentHashMap<Position, MapInformation> getMap(){
        ConcurrentHashMap<Position, MapInformation> map = new ConcurrentHashMap<>();
        map.put(new Position(0,0), getMapInformation());
        return map;
    }

    private MapInformation getMapInformation(){
        MapInformation mapInformation = new MapInformation();
        mapInformation.setIsWall(Boolean.FALSE);
        mapInformation.setDirection(Direction.N);
        mapInformation.setIsCrashed(Boolean.FALSE);
        return mapInformation;
    }

    private CarDto getCarDto(){
        return new CarDto("testCarName", CarType.NORMAL);
    }

    private CarSetup getCarSetup(){
        return new CarSetup(getCarDto(),"testMapName", new Position(0,0));
    }

    private CarMoveDto getCarMove(){
        return new CarMoveDto(getCarPk(),getMove());
    }

    private CarPk getCarPk(){
        return new CarPk("testCarName",CarType.NORMAL);
    }

    private Move getMove(){
        return Move.TURN_LEFT;
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}