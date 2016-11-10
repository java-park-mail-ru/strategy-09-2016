package ru.mail.park.game;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by victor on 02.11.16.
 */
public class GameBoard {
    private static final Integer BOARDHIGHT = 13;
    private static final Integer BOARDWIGHT = 13;
    private static final Integer NUMBEFOFCELL = 117;
    private AbstractCell[][] boardMap = new AbstractCell[BOARDHIGHT][BOARDWIGHT];
    private Pirat[] pirats = new Pirat[3];
    private Ship ship;

    public GameBoard() {
        final Vector<AbstractCell> cellIdPool = new Vector<>();
        for(int i = 0; i < NUMBEFOFCELL; ++i) {
            cellIdPool.add(new BoardCell(i));
        }
        Collections.shuffle(cellIdPool);
        Integer currentElement = 0;
        for(int i = 1; i < 12; ++i) {
            for( int j = 1; j < 12; ++j) {
                if(!(i==1&&j==1)&&!(i==1&&j==11)&&!(i==11&&j==11)&&!(i==11&&j==1)) {
                    cellIdPool.get(currentElement).setNeighbors(new CoordPair(i,j));
                    boardMap[i][j] = cellIdPool.get(currentElement);
                    ++currentElement;
                }
            }
        }
        Integer coastId = NUMBEFOFCELL;
        for(int i = 1; i < 12; ++i) {
            boardMap[i][0]=new CoastCell(coastId, new CoordPair(i,0));
            ++coastId;
        }
        boardMap[11][1]=new CoastCell(coastId, new CoordPair(11,1));
        ++coastId;
        for(int j = 1; j < 12; ++j) {
            boardMap[12][j]=new CoastCell(coastId, new CoordPair(12,j));
            ++coastId;
        }
        boardMap[11][11]=new CoastCell(coastId, new CoordPair(11,11));
        ++coastId;
        for(int i = 11; i >0; --i) {
            boardMap[i][12]=new CoastCell(coastId, new CoordPair(i,12));
            ++coastId;
        }
        boardMap[1][11]=new CoastCell(coastId, new CoordPair(1,11));
        ++coastId;
        for(int j = 11; j > 0; --j) {
            boardMap[0][j]=new CoastCell(coastId, new CoordPair(0,j));
            ++coastId;
        }
        boardMap[1][1]=new CoastCell(coastId, new CoordPair(1,1));
        ++coastId;

        boardMap[0][0]=new MockCell(-1, new CoordPair(0,0));
        boardMap[12][0]=new MockCell(-2, new CoordPair(12,0));
        boardMap[12][12]=new MockCell(-3, new CoordPair(12,12));
        boardMap[0][12]=new MockCell(-4, new CoordPair(0,12));

        generatePirat(0);
        generatePirat(1);
        generatePirat(2);

        setShip(0,new CoordPair(0,6), new CoordPair(0,1));
    }

    public CoordPair getShipCord(){
        return ship.getLocation();
    }

    private void setShip(Integer id, CoordPair location, CoordPair orientation){
        ship = new Ship(id,location, orientation);
        boardMap[location.getX()][location.getY()].setUnderShip(true);
    }

    private void generatePirat(Integer piratId){
        final Random random = new Random();
        Integer x;
        Integer y;
        do {
            x = random.nextInt(BOARDHIGHT-2)+1;
            y = random.nextInt(BOARDWIGHT-2)+1;
        } while(boardMap[x][y].getId()>=117 );
        pirats[piratId] = new Pirat(piratId, new CoordPair(x, y));
        boardMap[x][y].setPiratId(piratId);
    }

    public Integer movePirat(Movement piratMove){
        if(isCellPlacedNearPirat(piratMove.getPiratId(),piratMove.getTargetCell())){ //начальная и конечная клетки заданны корректно
            final Integer starterX = piratMove.getStartCell().getX();
            final Integer starterY = piratMove.getStartCell().getY();
            final Integer targetX = piratMove.getTargetCell().getX();
            final Integer targetY = piratMove.getTargetCell().getY();
            if(boardMap[targetX][targetY].isUnderShip && (Math.abs(starterX-targetX)+Math.abs(starterY-targetY))>1){
                return -1; //мы пытаемся взойти на корабль с диагональной клетки
            }
            if(boardMap[starterX][starterY].piratLeave(piratMove.getPiratId())){
                //пират успешно покинул клетку
                pirats[piratMove.getPiratId()].setLocation(piratMove.getTargetCell());
                boardMap[targetX][targetY].setPiratId(piratMove.getPiratId());
                return 0;
            }
            return -1; //пирата не было в клетке
        }
        return -2; // клетки не являлись соседями
    }

    public Boolean moveShip(CoordPair direction){

        for(CoordPair tempPair:ship.getAvaliableDirection()){
            if(CoordPair.equals(tempPair,direction)){ // такое направление вообще возможно
                if(boardMap[CoordPair.sum(ship.getLocation(),direction).getX()]
                        [CoordPair.sum(ship.getLocation(),direction).getY()].getId()>0){ //и мы не попали на угол
                    for(Integer piratId:boardMap[ship.getLocation().getX()][ship.getLocation().getY()].getPiratIds()) { //айди всех пиратов на корабле
                        pirats[piratId].setLocation(CoordPair.sum(ship.getLocation(),direction));
                        boardMap[CoordPair.sum(ship.getLocation(),direction).getX()]
                                [CoordPair.sum(ship.getLocation(),direction).getY()].setPiratId(piratId);
                    }
                    boardMap[ship.getLocation().getX()][ship.getLocation().getY()].setUnderShip(false);
                    ship.setLocation(direction);
                    boardMap[ship.getLocation().getX()][ship.getLocation().getY()].setUnderShip(true);
                    ship.resetNeighbors();
                    return true;
                }
            }
        }
        return false;
    }

    public AbstractCell getCell(CoordPair cellCord){
        return boardMap[cellCord.getX()][cellCord.getY()];
    }

    public CoordPair[] getCellNeighborsByPirat(Integer piratId){
        if(!getCell(getPiratCord(piratId)).getUnderShip()) {
            return getCell(getPiratCord(piratId)).getNeighbors();
        } else {
            return ship.getNeighbors();
        }
    }

    public Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell){
        if(!getCell(getPiratCord(piratId)).getUnderShip()) {
            return getCell(getPiratCord(piratId)).isNeighbors(targetCell);
        } else {
            return ship.isNeighbors(targetCell);
        }
    }

    public Integer getBoardMapId(Integer x, Integer y) {
        return boardMap[x][y].getId();
    }

    public Integer isPirat(CoordPair cord) { //эта штука говорит, есть ли пират в выбранной клетке
        for(Pirat pirat : pirats)
        if(pirat.getLocation().getX().equals(cord.getX()) && pirat.getLocation().getY().equals(cord.getY())){
            return pirat.getId();
        }
        return -1;
    }

    public Pirat getPirat(Integer piratId) {
        return pirats[piratId];
    }

    public CoordPair getPiratCord(Integer piratId) {
        if(piratId<3){
            return pirats[piratId].getLocation();
        } else {
            return null;
        }
    }
}
