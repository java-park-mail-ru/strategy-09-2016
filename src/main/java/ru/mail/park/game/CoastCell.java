package ru.mail.park.game;

/**
 * Created by victor on 03.11.16.
 */
public class CoastCell extends AbstractCell {
    private final static Integer NUMNEIGHBORS = 2;

    public CoastCell(Integer id) {
        this.id = id;

    }

    public void setNeighbors(CoordPair MyCord) {
        if(MyCord.getX()==0) {

        }
    }

    public Integer getId() {
        return id;
    }
}
