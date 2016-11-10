package ru.mail.park.game;

/**
 * Created by victor on 05.11.16.
 */
public class MockCell extends AbstractCell {
    private final static Integer NUMNEIGHBORS = 0;

    public MockCell(Integer id) {
        super.id = id;
        super.cord = null;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
    }

    public MockCell(Integer id, CoordPair MyCord) {
        super.id = id;
        super.cord = MyCord;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        super.isUnderShip = false;
        super.canBeFinal = false;
    }

    @Override
    public String getView(){
        return "  X";
    }
}
