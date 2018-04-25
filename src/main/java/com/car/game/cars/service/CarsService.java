package com.car.game.cars.service;

import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.repository.CarHistoryrepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.ActualInformation;
import com.car.game.game.FieldPosition;
import com.car.game.game.FieldlInformation;
import com.car.game.game.MapInGame;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.car.game.common.enums.CarType.*;
import static com.car.game.common.enums.Direction.*;
import static com.car.game.common.enums.Move.FORWARD;
import static com.car.game.common.enums.Move.TURN_LEFT;

@Log
@Service
public class CarsService {

    private CarRepository carRepository;
    private CarHistoryrepository carHistoryrepository;
    private CarsAssembler carsAssembler;
    private ActualInformation actualInformation = ActualInformation.getActualInformation();

    @Autowired
    public CarsService(CarRepository carRepository, CarsAssembler carsAssembler, CarHistoryrepository carHistoryrepository) {
        this.carRepository = carRepository;
        this.carsAssembler = carsAssembler;
        this.carHistoryrepository = carHistoryrepository;
    }

    @Transactional
    public boolean addCar(CarInformation carInformation){
        if( !exist(carInformation)){
            Car car = Car.builder()
                    .type(carInformation.getType())
                    .name(carInformation.getName())
                    .build();
            carRepository.save(car);
            log.info("New " + carInformation.getName() + " car was stored in database");
            return true;
        }
        log.warning("Given car already exist in database!");
        return false;
    }

    private boolean exist(CarInformation carInformation){
        Car car = carRepository.findCarByName(carInformation.getName());
        return (car == null) ? false : true;
    }

    public List<Car> getCars(){
        return carRepository.findAll();
    }

    public List<Move> getCarHistory(String name){
        return carHistoryrepository.findAllCarMovements(name);
    }
    public boolean deleteCar(CarInformation carInformation) {
        Car car = carRepository.findCarByName(carInformation.getName());
        if(car!=null){
            carRepository.delete(new Car(carInformation.getName()));
            log.info("Car " + carInformation.getName() + " has been removed from database");
            return true;
        }
        log.warning("Problem with removing car: "+ carInformation.getName());
        return false;
    }

    @Transactional
    public boolean addCarToMap(CarSetup carSetup) {
        Car car = carRepository.findCarByName(carSetup.getCar().getName());
        if(car==null){
            log.info("Car is not in DB");
            return false;
        }
        if(!car.getMapName().equals("")){
            log.info("Car is using in map :"+ car.getMapName());
            return false;
        }
        if(!isPositionOnMap(carSetup))
        {
            log.info("Bad fieldPosition");
            return false;
        }
        FieldlInformation fieldlInformation = actualInformation.getMapInformation(carSetup.getFieldPosition(),carSetup.getMapName());
        Boolean isWall = fieldlInformation.getIsWall();
        Boolean isCar = fieldlInformation.getCarName()!=null?true:false;
        if(isWall||isCar||exist(carSetup.getCar())){
            log.info("Cant place car on given field");
            return false;
        }
        if(!isPositionOnMap(carSetup)){
            log.info("Out of map");
            return false;
        }
            log.info("Car was added to game");
            updateCarInDB(carSetup);
            return true;
    }

    private boolean isPositionOnMap(CarSetup car){
        MapInGame map =actualInformation.getMapByName(car.getMapName());
        int size= 0;
        if(map==null){
            log.info("Map is not started");
            return false;
        }
        size = map.getSize();
        if(size<= car.getFieldPosition().getX()||size<= car.getFieldPosition().getY()){
            return false;
        }
        return true;
    }

    private void updateCarInDB(CarSetup carSetup){
        Car car =  Car.builder()
                .name(carSetup.getCar().getName())
                .mapName(carSetup.getMapName())
                .type(carSetup.getCar().getType())
                .build();
        carRepository.save(car);
        log.info("Car was added to game");
    }

    private CarHistory prepareCarHistoryMove(Move move, CarMove carMove){
        CarHistory carHistoryMove = new CarHistory();
        Car car = new Car();
        car.setType(carMove.getType());
        car.setName(carMove.getName());
        carHistoryMove.setMove(move);
        carHistoryMove.setCar(car);

        return carHistoryMove;
    }

    private void saveCarHistoryMove(Move move, CarMove carMove){
        CarHistory carHistory = prepareCarHistoryMove(move, carMove);
        carHistoryrepository.save(carHistory);
    }

    public void moveCar(CarMove carMove) {
        FieldlInformation fieldlInformation = actualInformation.getFieldInformationByCar(carMove);
        FieldPosition fieldPosition = actualInformation.getCarPositionByCar(carMove);
        MapInGame map = actualInformation.getMapByName(carMove.getMapName());
        if(fieldlInformation==null||fieldPosition==null){
            return;
        }
        // Changing only direction, no movement
        if(carMove.getMove()!=FORWARD){
            fieldlInformation.setDirection(updateDirection(FORWARD, fieldlInformation));
            map.getMap().replace(fieldPosition, fieldlInformation);
            saveCarHistoryMove(carMove.getMove(), carMove);
            return;
        }
        saveCarHistoryMove(FORWARD, carMove);

        FieldPosition futureFieldPosition = checkFuturePosition(fieldlInformation, fieldPosition,map.getSize());
        if(futureFieldPosition == null){
            log.warning("Out of the map!");
            return;
        }
        FieldlInformation futureFieldlInformation = actualInformation.getMapInformation(futureFieldPosition, carMove.getMapName());
        //IF WALL I DEAD
        if(futureFieldlInformation.getIsWall()){
            clearField(fieldPosition,map);
            return;
        }
        //CHANGE PLACE
        if(futureFieldlInformation==null){
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition,map);
            return;
        }
        // CRASH IN ANTOHER CAR
        resolveConflict(
                fieldPosition,
                fieldlInformation,
                futureFieldPosition,
                futureFieldlInformation,
                map);
    }

    public void resolveConflict(FieldPosition fieldPosition,
                                FieldlInformation fieldlInformation,
                                FieldPosition futureFieldPosition,
                                FieldlInformation futureFieldlInformation,
                                MapInGame map){


        if(NORMAL == futureFieldlInformation.getType() && TRUCK == fieldlInformation.getType()){
            // DEAD ENEMY
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition,map);
            return;
        }

        if(TRUCK == futureFieldlInformation.getType() && NORMAL == fieldlInformation.getType()){
            // I DEAD
            clearField(fieldPosition,map);
            return;
        }

        if(futureFieldlInformation.getType() == fieldlInformation.getType()){
            // DEAD BOTH
            clearField(fieldPosition,map);
            clearField(futureFieldPosition,map);
            return;
        }

        if(RACING != futureFieldlInformation.getType() && RACING == fieldlInformation.getType()){
            // I DEAD
            clearField(fieldPosition,map);
            return;
        }
    }

    public void clearField( FieldPosition fieldPosition,
                            MapInGame map){
        map.getMap().remove(fieldPosition);
    }

    public Direction updateDirection(Move move, FieldlInformation fieldlInformation){
        Direction actualDirection = fieldlInformation.getDirection();
        Direction futureDirection;

        switch (actualDirection){
            case S:{
                futureDirection = move.equals(TURN_LEFT) ? E : W;
                break;
            }
            case N:{
                futureDirection = move.equals(TURN_LEFT) ? W : E;
                break;
            }
            case W:{
                futureDirection = move.equals(TURN_LEFT) ? S : N;
                break;
            }
            case E:{
                futureDirection = move.equals(TURN_LEFT) ? N : S;
                break;
            }
            default:{
                futureDirection = actualDirection;
            }
        }

        return futureDirection;
    }

    public FieldPosition checkFuturePosition(FieldlInformation fieldlInformation,
                                             FieldPosition fieldPosition,
                                             int mapSize){
        int distance = fieldlInformation.getType()==RACING?2:1;
        FieldPosition fieldPositionAfterMove;
        switch (fieldlInformation.getDirection()){
            case E:{
                fieldPositionAfterMove = new FieldPosition(fieldPosition.getX()+distance, fieldPosition.getY());
                break;
            }
            case N:{
                fieldPositionAfterMove = new FieldPosition(fieldPosition.getX(), fieldPosition.getY()+distance);
                break;
            }
            case W:{
                fieldPositionAfterMove = new FieldPosition(fieldPosition.getX()-distance, fieldPosition.getY());
                break;
            }
            case S:{
                fieldPositionAfterMove = new FieldPosition(fieldPosition.getX(), fieldPosition.getY()-distance);
                break;
            }
            default:{
                fieldPositionAfterMove = fieldPosition;
            }
        }

        if(mapSize<= fieldPosition.getX()||mapSize<= fieldPosition.getY()){
            return null;
        }
        return fieldPositionAfterMove;

    }
}
