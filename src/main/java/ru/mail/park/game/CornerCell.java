package ru.mail.park.game;

/**
 * Created by victor on 03.11.16.
 */
public class CornerCell extends AbstractCell {
    private final static Integer NUMNEIGHBORS = 0;

    public CornerCell(Integer id){
        super.id = id;
        super.cord = null;
    }

    public CornerCell(Integer id, CoordPair MyCord){
        super.id = id;
        super.cord = MyCord;
    }


}
