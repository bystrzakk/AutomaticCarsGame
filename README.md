# AutomaticCarsGame
Coding Interview Challenge

## DEV Team ###
- Mateusz Bystrzycki
- Adam Paczuski
- Piotr Kędzierski
- Łukasz Szelest

## Technology stack:

##### 1. Language:
    Java 8
##### 2. Framework:
    Springboot - 2.0.1
##### 3. ORM
    Spring data-jpa - Kay-SR6
##### 4. Build Tool
    Gradle - 4.6
##### 5. Tests: 
    JUnit 4
    AssertJ
##### 6. Libraries
    Lombok - 1.16.20
##### 7. Data Base
    H2 Database - 1.4.197
##### 8. Tools
    Swagger2 - 2.8.0

## API Documentation

## Endpoints:
### Car Controller
1. **Add new car:**
    >**Method:** POST **Request URL:** */car*

2. **Remove car:**
    >**Method:** DELETE **Request URL:** */car*

3. **Add car to game controller:**
    >**Method:** POST **Request URL:** */car-first-setup*

4. **Get movements history for given car:**
    >**Method:** GET **Request URL:** */car-history*

5. **Move car on map:**
    >**Method:** POST **Request URL:** */car-move*

5. **Delete car from the game:**
    >**Method:** DELETE **Request URL:** */car-remove-from-game*
    
5. **Repair car:**
    >**Method:** POST **Request URL:** */car-repair*
    
5. **Get all cars:**
    >**Method:** GET **Request URL:** */cars*
    

### Map Controller
1. **Add map:**
    >**Method:** POST **Request URL:** */map*

2. **Delete map:**
    >**Method:** DELETE **Request URL:** */map*

3. **Get all maps:**
    >**Method:** GET **Request URL:** */maps*

4. **Select the map:**
    >**Method:** POST **Request URL:** */selected-map*

5. **Unselect the map:**
    >**Method:** POST **Request URL:** */unselected-map*
