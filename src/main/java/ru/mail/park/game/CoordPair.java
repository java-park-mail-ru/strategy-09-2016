package ru.mail.park.game;

/**
 * Created by victor on 03.11.16.
 */
public class CoordPair {
    Integer x;
    Integer y;

    static CoordPair sum(CoordPair firstPair, CoordPair secondPair){
        return new CoordPair(firstPair.getX()+ secondPair.getX(), firstPair.getY() + secondPair.getY());
    }
    static  Boolean equals(CoordPair firstPair, CoordPair secondPair){
        return firstPair.getX()==secondPair.getX()&&firstPair.getY()==secondPair.getY();
    }

    public CoordPair(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
