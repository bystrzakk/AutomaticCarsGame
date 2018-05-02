package com.car.game.cars.service;

import com.car.exception.RemoveCarException;
import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.common.enums.Direction;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.repository.CarHistoryRepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.ActualInformation;
import com.car.game.game.FieldPosition;
import com.car.game.game.FieldlInformation;
import com.car.game.game.MapInGame;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.car.game.common.enums.CarType.*;
import static com.car.game.common.enums.Direction.*;
import static com.car.game.common.enums.Move.FORWARD;
import static com.car.game.common.enums.Move.TURN_LEFT;

@Log
@Service
public class CarService {

    private CarRepository carRepository;
    private CarHistoryRepository carHistoryRepository;
    private ActualInformation actualInformation = ActualInformation.getActualInformation();
    private SimpMessageSendingOperations messageTemplate;

    @Autowired
    public CarService(CarRepository carRepository, CarHistoryRepository carHistoryRepository, SimpMessageSendingOperations messageTemplate) {
        this.carRepository = carRepository;
        this.carHistoryRepository = carHistoryRepository;
        this.messageTemplate = messageTemplate;
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
        Car car = findCarByName(carInformation.getName());
        return (car == null) ? false : true;
    }

    public List<Car> getCars(){
        return carRepository.findAll();
    }

    public List<Move> getCarHistory(String name){
        return carHistoryRepository.findAllCarMovements(name);
    }

    public void deleteCar(String carName) {
        Car car = findCarByName(carName);
        if(car != null){
            carRepository.delete(car);
            log.info("Car " + carName + " has been removed from database");
        } else {
            log.warning("Problem with removing car: " + carName);
            throw new RemoveCarException("Remove car exception");
        }
    }

    @Transactional
    public boolean addCarToMap(CarSetup carSetup) {
        Car car = findCarByName(carSetup.getCarName());
        if(car == null){
            return false;
        }
        if(car.getMapName()!=null){
            log.info("Car is using in map :"+ car.getMapName());
            return false;
        }
        if(!isPositionOnMap(carSetup))
        {
            log.info("Bad field Position");
            return false;
        }
        FieldlInformation fieldlInformation = actualInformation.getMapInformation(carSetup.getFieldPosition(),carSetup.getMapName());
        Boolean isWall = fieldlInformation.getIsWall();
        Boolean isCar = fieldlInformation.getCarName()!=null?true:false;
        if(isWall||isCar){
            log.info("Cant place car on given field");
            return false;
        }
        if(!isPositionOnMap(carSetup)){
            log.info("Out of map");
            return false;
        }
        carRepository.update(car.getName(),carSetup.getMapName(),car.getType());
        fieldlInformation.setCarName(carSetup.getCarName());
        MapInGame map = actualInformation.getMapByName(carSetup.getMapName());
        map.getMap().replace(carSetup.getFieldPosition(), fieldlInformation);
        log.info("Car was added to game");
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

    private CarHistory prepareCarHistoryMove(Move move, CarMove carMove){
        CarHistory carHistoryMove = new CarHistory();
        Car car = findCarByName(carMove.getCarName());
        carHistoryMove.setMove(move);
        carHistoryMove.setCar(car);

        return carHistoryMove;
    }

    private void saveCarHistoryMove(Move move, CarMove carMove){
        CarHistory carHistory = prepareCarHistoryMove(move, carMove);
        carHistoryRepository.save(carHistory);
    }

    public void moveCar(CarMove carMove) {
        Car car = findCarByName(carMove.getCarName());

        if(car == null){
            return;
        }

        FieldlInformation fieldlInformation = actualInformation.getFieldInformationByCar(carMove, car.getMapName());
        FieldPosition fieldPosition = actualInformation.getCarPositionByCar(carMove, car.getMapName());
        if(fieldlInformation==null||fieldPosition==null){
            log.info("Car not found");
            return;
        }
        MapInGame map = actualInformation.getMapByName(car.getMapName());
        // Changing only direction, no movement
        if(carMove.getMove()!=FORWARD){
            fieldlInformation.setDirection(updateDirection(FORWARD, fieldlInformation));
            map.getMap().replace(fieldPosition, fieldlInformation);
            saveCarHistoryMove(carMove.getMove(), carMove);
            log.info("Car turn "+ carMove.getMove());
            return;
        }
        saveCarHistoryMove(FORWARD, carMove);

        FieldPosition futureFieldPosition = checkFuturePosition(fieldlInformation, fieldPosition,map.getSize());
        FieldlInformation futureFieldlInformation = actualInformation.getMapInformation(futureFieldPosition, car.getMapName());

        if(futureFieldPosition == null||futureFieldlInformation==null){
            log.warning("Out of the map!");
            return;
        }
        //If wall car will crash
        if(futureFieldlInformation.getIsWall()){
            clearField(fieldPosition,map);
            log.info("Car crashed");
            return;
        }
        //Change place
        if(futureFieldlInformation==null){
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition,map);
            log.info("Car CHANGE PLACE");
            return;
        }
        //Crash with another car
        resolveConflict(
                fieldPosition,
                fieldlInformation,
                futureFieldPosition,
                futureFieldlInformation,
                map);

        // Emit map state
        messageTemplate.convertAndSend("/subscribe/map/" + car.getMapName(), map);
    }

    public void resolveConflict(FieldPosition fieldPosition,
                                FieldlInformation fieldlInformation,
                                FieldPosition futureFieldPosition,
                                FieldlInformation futureFieldlInformation,
                                MapInGame map){


        if(NORMAL == futureFieldlInformation.getType() && TRUCK == fieldlInformation.getType()){
            // Crash enemy
            map.getMap().replace(futureFieldPosition, fieldlInformation);
            clearField(fieldPosition,map);
            log.info("Crash enemy");
            return;
        }

        if(TRUCK == futureFieldlInformation.getType() && NORMAL == fieldlInformation.getType()){
            // I crashed
            clearField(fieldPosition,map);
            log.info("Car crashed");
            return;
        }

        if(futureFieldlInformation.getType() == fieldlInformation.getType()){
            // Crash both
            clearField(fieldPosition,map);
            clearField(futureFieldPosition,map);
            log.info("Crash both");
            return;
        }

        if(RACING != futureFieldlInformation.getType() && RACING == fieldlInformation.getType()){
            // Car crash
            clearField(fieldPosition,map);
            log.info("Car crashed");
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

    protected Car findCarByName(String carName){
        Car car = carRepository.findCarByName(carName);

        if (car == null) {
            log.warning("Car " + carName + " is not exist in database.");
            return null;
        } else {
            return car;
        }
    }

    public boolean deleteCarFromMap(String carName) {
        Car car = findCarByName(carName);

        if (car == null) {
            return false;
        }

        if (null == car.getMapName()) {
            log.warning("Car " + carName+ " is not exist in any game.");
            return false;
        }

        CarMove carMove = new CarMove();
        carMove.setCarName(carName);

        ActualInformation actualInformation = ActualInformation.getActualInformation();
        FieldPosition fieldPosition = actualInformation.getCarPositionByCar(carMove, car.getMapName());
        clearField(fieldPosition, actualInformation.getMapByName(car.getMapName()));

        car.setMapName(null);
        carRepository.save(car);

        log.info("Car was deleted from game");
        return true;
    }

    public boolean repairCar(String carName) {
        Car car = findCarByName(carName);
        if (car == null) {
            return false;
        }

        if (!car.isCrashed()) {
            log.warning("Car " + carName+ " is not crashed.");
            return false;
        }

        car.setCrashed(false);
        carRepository.save(car);

        log.info("Car is repaired.");
        return true;
    }
}
