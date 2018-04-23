package com.car.game.cars.service;

import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.dto.CarsDto;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarPk;
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

    @Transactional
    public boolean addCar(CarsDto carsDto){
        if( !exist(carsDto)){
            Car car = new Car();
            CarPk carPk = getCarPk(carsDto);
            car.setCarPk(carPk);
            carRepository.save(car);
            log.info("New " + carsDto.getName() + " car was stored in Database");

            return true;
        }
        log.warning("Given Car already exist in Database!");

        return false;

    }

    private boolean exist(CarsDto carsDto){
        CarPk carPk = getCarPk(carsDto);
        Car car = carRepository.findCarByCarPk(carPk);
        if(car == null){
            return false;
        }
        return true;
    }

    public List<Car> getCars(){
        return carRepository.findAll();
    }

    public void deleteCar(CarsDto carsDto) {

        CarPk carPk = getCarPk(carsDto);
        boolean exist = carRepository.existsById(carPk);
        if(exist){
            carRepository.deleteById(carPk);
        }
    }


    private CarPk getCarPk(CarsDto carsDto) {
        CarPk carPk = new CarPk();
        carPk.setName(carsDto.getName());
        carPk.setType(carsDto.getType());
        return carPk;
    }

    private CarPk getCarPk(String name , CarType carType) {
        CarPk carPk = new CarPk();
        carPk.setName(name);
        carPk.setType(carType);
        return carPk;
    }

    @Transactional
    public void addCarToMap(CarSetup carSetup) {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        MapInformation mapInformation = actualInformation.getMapInformation(carSetup.getPosition());
        Boolean isWall = actualInformation.isWall(mapInformation);
        Boolean isCar = actualInformation.isCar(mapInformation);
        if(isCar&&isWall&&exist(carSetup.getCar())){
            log.info("Car was added to game");
            updateCarInDB(carSetup);
        }
        log.info("Cant place car on given field");
    }

    private void updateCarInDB(CarSetup carSetup){
        Car car = new Car();
        CarPk carPk = getCarPk(carSetup.getCar().getName(), carSetup.getCar().getType());
        car.setCarPk(carPk);
        car.setMapName(carSetup.getMapName());
        carRepository.save(car);
        log.info("Car was added to game");
    }

    public void moveCar(CarMove carMove) {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        MapInformation mapInformation = actualInformation.getMapInformationByCar(carMove.getCar());
        Position position = actualInformation.getCarPositionByCar(carMove.getCar());

        // Changing only direction, no movement
        if(carMove.getMove()!=FORWARD){
            mapInformation.setDirection(updateDirection(FORWARD, mapInformation));
            actualInformation.updateConcurentHashMap(position, mapInformation);;
            return;
        }

        Position futurePosition = checkFuturePosition(mapInformation.getDirection(),position);
        if(futurePosition == null){
            log.warning("Out of the map!");
            return;
        }
        MapInformation futureMapInformation = actualInformation.getMapInformation(futurePosition);

        if(futureMapInformation.getIsWall()){
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            return;
        }

        if(futureMapInformation.getCar() == null){
            actualInformation.updateConcurentHashMap(futurePosition, mapInformation);
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            return;
        }

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
            // GINIE PRZECIWNIK
            actualInformation.updateConcurentHashMap(futurePosition, mapInformation);
            clearField(mapInformation, position);
            return;
        }

        if(TRUCK == enemyCar.getType() && NORMAL == car.getType()){
            // GINE JA
            clearField(mapInformation, position);
            return;
        }

        if(enemyCar.getType() == car.getType()){
            // GINA OBYDWA
            mapInformation.setCar(null);
            mapInformation.setDirection(N);
            actualInformation.updateConcurentHashMap(position, mapInformation);
            actualInformation.updateConcurentHashMap(futurePosition, mapInformation);
            return;
        }

        if(RACING != enemyCar.getType() && RACING == car.getType()){
            // GINNE JA
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
                if(move.equals(TURN_LEFT)){
                    futureDirection = E;
                }
                else {
                    futureDirection = W;
                }
                break;
            }
            case N:{
                if(move.equals(TURN_LEFT)){
                    futureDirection = W;
                }
                else {
                    futureDirection = E;
                }
                break;
            }
            case W:{
                if(move.equals(TURN_LEFT)){
                    futureDirection = S;
                }
                else {
                    futureDirection = N;
                }
                break;
            }
            case E:{
                if(move.equals(TURN_LEFT)){
                    futureDirection = N;
                }
                else {
                    futureDirection = S;
                }
                break;
            }
            default:{
                futureDirection = actualDirection;
            }
        }

        return futureDirection;
    }


    public Position checkFuturePosition(Direction direction, Position position){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        Position positionAfterMove ;
        switch (direction){
            case E:{
                positionAfterMove = new Position(position.getX()+1,position.getY());
                break;
            }
            case N:{
                positionAfterMove = new Position(position.getX(),position.getY()+1);
                break;
            }
            case W:{
                positionAfterMove = new Position(position.getX()-1,position.getY());
                break;
            }
            case S:{
                positionAfterMove = new Position(position.getX(),position.getY()-1);
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
