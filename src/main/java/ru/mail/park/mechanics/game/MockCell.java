package ru.mail.park.mechanics.game;

public class MockCell extends AbstractCell {
    private static final Integer NUMNEIGHBORS = 0;

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
