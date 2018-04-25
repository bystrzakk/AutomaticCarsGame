package com.car.game.cars.service;

import com.car.game.cars.dto.CarDto;
import com.car.game.cars.dto.CarMoveDto;
import com.car.game.cars.dto.CarSetup;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.model.CarPk;
import com.car.game.common.repository.CarHistoryrepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.ActualInformation;
import com.car.game.game.MapInformation;
import com.car.game.game.Position;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsService {

    private CarRepository carRepository;
    private CarHistoryrepository carHistoryrepository;
    private CarsAssembler carsAssembler;

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
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        MapInformation mapInformation = actualInformation.getMapInformation(carSetup.getPosition());
        Boolean isWall = actualInformation.isWall(mapInformation);
        Boolean isCar = actualInformation.isCar(mapInformation);
        if(isWall||isCar||exist(carSetup.getCar())){
            log.info("Cant place car on given field");
            return false;
        }
        if(!checkPositionIsOnMap(carSetup.getPosition())){
            log.info("Out of map");
            return false;
        }
            log.info("Car was added to game");
            updateCarInDB(carSetup);
            return true;
    }

    private boolean checkPositionIsOnMap(Position position){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        if(actualInformation.getMapSize()<=position.getX()||actualInformation.getMapSize()<=position.getY()){
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

    private CarHistory prepareCarHistoryMove(Move move, CarMoveDto carMoveDto){
        CarHistory carHistoryMove = new CarHistory();
        Car car = new Car();
        car.setCarPk(carMoveDto.getCar());
        carHistoryMove.setMove(move);
        carHistoryMove.setCar(car);

        return carHistoryMove;
    }

    public void moveCar(CarMoveDto carMoveDto) {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        MapInformation mapInformation = actualInformation.getMapInformationByCar(carMoveDto.getCar());
        Position position = actualInformation.getCarPositionByCar(carMoveDto.getCar());

        // Changing only direction, no movement
        if(carMoveDto.getMove() != FORWARD){
            mapInformation.setDirection(updateDirection(FORWARD, mapInformation));
            actualInformation.updateConcurentHashMap(position, mapInformation);
            prepareCarHistoryMove(carMoveDto.getMove(), carMoveDto);
            return;
        }

        prepareCarHistoryMove(FORWARD, carMoveDto);

        Position futurePosition = checkFuturePosition(mapInformation, position);
        if(futurePosition == null){
            log.warning("Out of the map!");
            return;
        }
        MapInformation futureMapInformation = actualInformation.getMapInformation(futurePosition);
        //IF WALL
        if(futureMapInformation.getIsWall()){
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            return;
        }
        //CHANGE PLACE
        if(futureMapInformation.getCar()==null){
            actualInformation.updateConcurentHashMap(futurePosition, mapInformation);
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            return;
        }
        // CRASH IN ANTOHER CAR
        CarPk car = mapInformation.getCar();
        CarPk enemyCar = futureMapInformation.getCar();

        resolveConflict(car, enemyCar, futurePosition, position, mapInformation);
    }

    public void resolveConflict(CarPk car,
                                CarPk enemyCar,
                                Position futurePosition,
                                Position position,
                                MapInformation mapInformation){

        ActualInformation actualInformation = ActualInformation.getActualInformation();

        if(NORMAL == enemyCar.getType() && TRUCK == car.getType()){
            // DEAD ENEMY
            actualInformation.updateConcurentHashMap(futurePosition,mapInformation);
            clearField(mapInformation,position);
            return;
        }

        if(TRUCK == enemyCar.getType() && NORMAL == car.getType()){
            // I DEAD
            clearField(mapInformation, position);
            return;
        }

        if(enemyCar.getType() == car.getType()){
            // DEAD BOTH
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            actualInformation.updateConcurentHashMap(futurePosition, mapInformation);
            return;
        }

        if(RACING != enemyCar.getType() && RACING == car.getType()){
            // I DEAD
            clearField(mapInformation,position);
            return;
        }
    }

    public void clearField(MapInformation mapInformation, Position position){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        mapInformation.setCar(null);
        mapInformation.setDirection(N);
        actualInformation.updateConcurentHashMap(position, mapInformation);
    }

    public Direction updateDirection(Move move, MapInformation mapInformation){
        Direction actualDirection = mapInformation.getDirection();
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

    public Position checkFuturePosition(MapInformation mapInformation, Position position){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        int distance = mapInformation.getCar().getType()==RACING?2:1;
        Position positionAfterMove ;
        switch (mapInformation.getDirection()){
            case E:{
                positionAfterMove = new Position(position.getX()+distance,position.getY());
                break;
            }
            case N:{
                positionAfterMove = new Position(position.getX(),position.getY()+distance);
                break;
            }
            case W:{
                positionAfterMove = new Position(position.getX()-distance,position.getY());
                break;
            }
            case S:{
                positionAfterMove = new Position(position.getX(),position.getY()-distance);
                break;
            }
            default:{
                positionAfterMove = position;
            }
        }

        if(actualInformation.getMapSize()<=position.getX()||actualInformation.getMapSize()<=position.getY()){
            return null;
        }
        return positionAfterMove;

    }
}
