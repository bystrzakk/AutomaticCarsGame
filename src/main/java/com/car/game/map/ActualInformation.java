package com.car.game.map;

public class ActualInformation {

    private static ActualInformation getActulaInformation = new ActualInformation();
    private static String  actualMapName = null;
    private ActualInformation() {
    }

    public static ActualInformation getGetActulaInformation() {
        return getActulaInformation;
    }

    public  String getActualMapName() {
        return actualMapName;
    }

    public  void setActualMapName(String actualMapName) {
        ActualInformation.actualMapName = actualMapName;
    }
}
