package ru.mail.park.game;

/**
 * Created by victor on 03.11.16.
 */
abstract public class AbstractCell {
    protected Integer id;
    protected CoordPair cord;
    protected CoordPair[] neighbors;

    public Integer getId() {
        return id;
    }

    public void setNeighbors(CoordPair MyCord) {

    }
}
