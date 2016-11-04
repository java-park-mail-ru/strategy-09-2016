package ru.mail.park.game;

/**
 * Created by victor on 03.11.16.
 */
public class BoardCell extends AbstractCell {
    private final static Integer NUMNEIGHBORS = 8;

    public BoardCell(Integer id){
        super.id = id;
        super.cord = null;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
    }

    public BoardCell(Integer id, CoordPair MyCord){
        super.id = id;
        super.cord = MyCord;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        //соседи - все 8 ближайших клеток
        setNeighbors(MyCord);
    }

    public CoordPair[] getAvailibleCell () {
        return neighbors;
    }

    public Integer getId() {
        return id;
    }

    public CoordPair getCord() {
        return cord;
    }

    @Override
    public void setNeighbors(CoordPair MyCord) {
        super.cord = MyCord;
        //соседи - все 8 ближайших клеток
        CoordPair tempPair = new CoordPair(MyCord.getX() - 1, MyCord.getY() );
        neighbors[0] = tempPair;

        for(int i = 0; i < 3; ++i) {
            tempPair = new CoordPair(MyCord.getX() + (i - 1), MyCord.getY() - 1);
            neighbors[1+i] = tempPair;
        }

        tempPair = new CoordPair(MyCord.getX() + 1, MyCord.getY());
        neighbors[4] = tempPair;

        for(int i = 0; i < 3; ++i ){
            tempPair = new CoordPair(MyCord.getX() + (1 - i), MyCord.getY() + 1);
            neighbors[5+i] = tempPair;
        }
    }
}
