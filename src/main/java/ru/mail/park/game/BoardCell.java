package ru.mail.park.game;

public class BoardCell extends AbstractCell {
    private static final Integer NUMNEIGHBORS = 8;

    public BoardCell(Integer id){
        super.id = id;
        super.cord = null;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        super.isUnderShip = false;
        super.canBeFinal = true;
    }

    @Override
    public void setNeighbors(CoordPair cordinateOfCell) {
        super.cord = cordinateOfCell;
        //соседи - все 8 ближайших клеток
        CoordPair tempPair = new CoordPair(cordinateOfCell.getX() - 1, cordinateOfCell.getY() );
        neighbors[0] = tempPair;

        for(int i = 0; i < 3; ++i) {
            tempPair = new CoordPair(cordinateOfCell.getX() + (i - 1), cordinateOfCell.getY() - 1);
            neighbors[1+i] = tempPair;
        }

        tempPair = new CoordPair(cordinateOfCell.getX() + 1, cordinateOfCell.getY());
        neighbors[4] = tempPair;

        for(int i = 0; i < 3; ++i ){
            tempPair = new CoordPair(cordinateOfCell.getX() + (1 - i), cordinateOfCell.getY() + 1);
            neighbors[5+i] = tempPair;
        }
    }

    @Override
    public String getView(){
        final StringBuilder builder = new StringBuilder();
        if(piratIds.length==0) {
            builder.append(id);
        } else {
            builder.append("(*)");
        }
        return builder.toString();
    }

}
