package com.car.game.cars.service;

import com.car.game.cars.dto.CarDto;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.model.CarPk;
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
    public boolean addCar(CarDto carDto){
        if( !exist(carDto)){
            Car car = new Car();
            CarPk carPk = carsAssembler.getCarPk(carDto);
            car.setCarPk(carPk);
            carRepository.save(car);
            log.info("New " + carDto.getName() + " car was stored in database");
            return true;
        }
        log.warning("Given car already exist in database!");
        return false;
    }

    private boolean exist(CarDto carDto){
        CarPk carPk = carsAssembler.getCarPk(carDto);
        Car car = carRepository.findCarByCarPk(carPk);
        return (car == null) ? false : true;
    }

    public List<Car> getCars(){
        return carRepository.findAll();
    }

    public List<Move> getCarHistory(String name){
        return carHistoryrepository.findAllCarMovements(name);
    }

    public boolean deleteCar(CarDto carDto) {
        CarPk carPk = carsAssembler.getCarPk(carDto);
        boolean exist = carRepository.existsById(carPk);
        if(exist){
            carRepository.deleteById(carPk);
            log.info("Car " + carDto.getName() + " has been removed from database");
            return true;
        }
        log.warning("Problem with removing car: "+ carDto.getName());
        return false;
    }

    @Transactional
    public boolean addCarToMap(CarSetup carSetup) {
        if(!isPositionOnMap(carSetup))
        {
            log.info("Bad fieldPosition");
            return false;
        }
        FieldlInformation fieldlInformation = actualInformation.getMapInformation(carSetup.getFieldPosition(),carSetup.getMapName());
        Boolean isWall = fieldlInformation.getIsWall();
        Boolean isCar = fieldlInformation.getCar()!=null?true:false;
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
        Car car = new Car();
        CarPk carPk = carsAssembler.getCarPk(carSetup.getCar());
        car.setCarPk(carPk);
        car.setMapName(carSetup.getMapName());
        carRepository.save(car);
        log.info("Car was added to game");
    }

    private CarHistory prepareCarHistoryMove(Move move, CarMove carMove){
        CarHistory carHistoryMove = new CarHistory();
        Car car = new Car();
        car.setCarPk(carMove.getCar());
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
            clearField(fieldPosition, fieldlInformation,map);
            return;
        }
        //CHANGE PLACE
        if(futureFieldlInformation.getCar()==null){
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition, fieldlInformation,map);
            return;
        }
        // CRASH IN ANTOHER CAR
        CarPk car = fieldlInformation.getCar();
        CarPk enemyCar = futureFieldlInformation.getCar();

        resolveConflict(car, enemyCar, futureFieldPosition, fieldPosition, fieldlInformation,map);
    }

    public void resolveConflict(CarPk car,
                                CarPk enemyCar,
                                FieldPosition futureFieldPosition,
                                FieldPosition fieldPosition,
                                FieldlInformation fieldlInformation,
                                MapInGame map){


        if(NORMAL == enemyCar.getType() && TRUCK == car.getType()){
            // DEAD ENEMY
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition,fieldlInformation,map);
            return;
        }

        if(TRUCK == enemyCar.getType() && NORMAL == car.getType()){
            // I DEAD
            clearField(fieldPosition,fieldlInformation,map);
            return;
        }

        if(enemyCar.getType() == car.getType()){
            // DEAD BOTH
            clearField(fieldPosition, fieldlInformation,map);
            clearField(futureFieldPosition, fieldlInformation,map);
            return;
        }

        if(RACING != enemyCar.getType() && RACING == car.getType()){
            // I DEAD
            clearField(fieldPosition,fieldlInformation,map);
            return;
        }
    }

    public void clearField( FieldPosition fieldPosition,
                            FieldlInformation fieldlInformation,
                            MapInGame map){
        fieldlInformation.setCar(null);
        fieldlInformation.setDirection(N);
        map.getMap().replace(fieldPosition,fieldlInformation);
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
        int distance = fieldlInformation.getCar().getType()==RACING?2:1;
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
