package ru.mail.park.mechanics.game;

public class CoordPair {
    private final Integer x;
    private final Integer y;

    public static CoordPair sum(CoordPair firstPair, CoordPair secondPair){
        return new CoordPair(firstPair.x+ secondPair.x, firstPair.y + secondPair.y);
    }
    public static Boolean equals(CoordPair firstPair, CoordPair secondPair){
        return firstPair.x.equals(secondPair.x)&&firstPair.y.equals(secondPair.y);
    }

    public CoordPair(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
