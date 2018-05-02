package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import com.car.game.common.repository.MapRepository;
import com.car.game.configuration.MockMethod;


import com.car.game.map.dto.MapRequestDto;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapServiceTest extends MockMethod{

    private MapRepository mapRepository;
    private MapService mapService;
    private MapAssembler mapAssembler;

    @Before
    public void setUp() throws Exception {
        mapRepository = mock(MapRepository.class);
        mapAssembler = mock(MapAssembler.class);
        mapService = new MapService(mapAssembler,mapRepository);
    }

    @Test
    public void shouldReturnAllMaps() throws Exception{
        when(mapRepository.findAll()).thenReturn(getAllMaps());
        final List<MapGame> mapList = mapService.getAllMaps();

        assertThat(mapList.get(0).getName()).isEqualTo(getMapGame(true, false).getName());
    }

    @Test
    public void shouldReturnFalseWhenAddIncorrectMap() throws Exception{
        when(mapAssembler.getMapGame(anyString(),anyString())).thenReturn(getMapGame(true, false));
        final boolean isMapAdded = mapService.addNewMap(getMapRequestDto("testMapName","0,0,1,"));

        assertThat(isMapAdded).isFalse();
    }

    @Test(expected = NumberFormatException.class)
    public void shouldReturnNumberFormatExceptionWhenAddWrongFormat() throws Exception{
        when(mapAssembler.getMapGame(anyString(),anyString())).thenReturn(getMapGame(true, false));
        mapService.addNewMap(getMapRequestDto("testMapName","0,0,a"));
    }

    @Test
    public void shouldReturnTrueForSelectedMap() throws Exception{
        when(mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(anyString())).thenReturn(getMapGame(false, false));
        final boolean selectMap = mapService.selectMap("testMapName");

        assertThat(selectMap).isTrue();
    }

    @Test
    public void shouldReturnFalseForSelectedMapIfNoMapInRepository() throws Exception{
        when(mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(anyString())).thenReturn(null);
        final boolean selectMap = mapService.selectMap("testMapName");

        assertThat(selectMap).isFalse();
    }

   @Test
   public void shouldReturnTrueIfMapExists() throws Exception{
       when(mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(anyString())).thenReturn(getMapGame(true, false));
       final boolean isMapExist = mapService.isMapExist("testMapName");

       assertThat(isMapExist).isTrue();
   }


    @Test
    public void shouldReturnFalseForRemoveIfMapIsNull() throws Exception{
        when(mapRepository.findByName(anyString())).thenReturn(null);
        final boolean isDeletedMap = mapService.deleteMap("testMapName");

        assertThat(isDeletedMap).isFalse();
    }

    private List<MapGame> getAllMaps(){
        return Arrays.asList(getMapGame(true, false));
    }

    private MapGame getMapGame(boolean used,boolean deleted){
        return new MapGame(1l,"testMapName","0,0,0,0", used,deleted);
    }

    private MapRequestDto getMapRequestDto(String name, String body){
        return new MapRequestDto(name, body);
    }
}
