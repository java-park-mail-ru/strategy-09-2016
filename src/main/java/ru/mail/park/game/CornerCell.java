package ru.mail.park.game;

public class CornerCell extends AbstractCell {
    private static final Integer NUMNEIGHBORS = 0;

    public CornerCell(Integer id){
        super.id = id;
        super.cord = null;
    }

    public CornerCell(Integer id, CoordPair myCord){
        super.id = id;
        super.cord = myCord;
    }


}
