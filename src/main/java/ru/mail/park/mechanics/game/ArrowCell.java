package ru.mail.park.game;

public class ArrowCell extends AbstractCell { //класс клетки со стрелкой
    private final static Integer NUMNEIGHBORS = 1; // пока не доделанный
    private final CoordPair directionOfArrow;

    public ArrowCell(Integer id, CoordPair direction){
        super.id = id;
        super.cord = null;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        super.isUnderShip = false;
        super.canBeFinal = false;
        this.directionOfArrow = direction;
    }

    @Override
    public void setNeighbors(CoordPair cordinateOfCell) {
        super.cord = cordinateOfCell;
        neighbors = new CoordPair[1];
        neighbors[0] = CoordPair.sum(cord,directionOfArrow);
    }

    @Override
    public String getView(){
        return "  <-";
    }
}
