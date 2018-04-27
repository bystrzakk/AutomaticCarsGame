package com.car.game.map;

import com.car.game.common.repository.MapRepository;
import com.car.game.configuration.TestConfig;
import com.car.game.map.dto.MapRequestDto;
import com.car.game.map.service.MapService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        this.mapService.addNewMap(getMapRequestDto("testMapName1","0,0,0,0"));
    }

    @Test
    public void testAddMap() throws Exception{
        mockMvc.perform(post("/map")
                .contentType(contentType)
                .content(json(getMapRequestDto("testMapName01","0,0,0"))))
                .andExpect(status().isCreated());
    }

    //TODO
//    @Test
//    public void testAddMapWithIncorrectFormatException() throws Exception{
//        mockMvc.perform(post("/map")
//                .contentType(contentType)
//                .content(json(getMapRequestDto("testMapName2","0,0,0,"))))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void testAddMapWithNumberFormatException() throws Exception{
        mockMvc.perform(post("/map")
                .contentType(contentType)
                .content(json(getMapRequestDto("testMapName2","0,0,0,a"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetMaps() throws Exception{
        mockMvc.perform(get("/maps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testSelectMap() throws Exception{
        mockMvc.perform(post("/selected-map").param("name", "testMapName1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnSelectMap() throws Exception{
        mockMvc.perform(post("/unselected-map").param("name", "testMapName1"))
                .andExpect(status().isOk());
    }




    private MapRequestDto getMapRequestDto(String name, String body){
        return new MapRequestDto(name, body);
    }


}