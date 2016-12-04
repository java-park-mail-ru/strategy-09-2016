package ru.mail.park.mechanics.game;

import ru.mail.park.mechanics.utils.MovementResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class GameBoard {
    private static final Integer BOARDHIGHT = 13;
    private static final Integer BOARDWIGHT = 13;
    private static final Integer ISLAND_HIGHT = BOARDHIGHT -2 ;
    private static final Integer ISLAND_WIGHT = BOARDWIGHT -2 ;
    private static final Integer NUMBEFOFCELL = 117;
    private AbstractCell[][] boardMap = new AbstractCell[BOARDHIGHT][BOARDWIGHT];
    private GamePlayer[] players = new GamePlayer[2];

    public GameBoard() {
        final Vector<AbstractCell> cellIdPool = new Vector<>();
        for(int i = 0; i < NUMBEFOFCELL; ++i) {
            cellIdPool.add(new BoardCell(i));
        }
        Collections.shuffle(cellIdPool);
        Integer currentElement = 0;
        for(int i = 1; i < ( ISLAND_HIGHT + 1 ); ++i) {
            for( int j = 1; j < ( ISLAND_WIGHT + 1 ); ++j) {
                if(!(i==1&&j==1)&&!(i==1&&j==ISLAND_HIGHT)&&
                        !(i==ISLAND_HIGHT&&j==ISLAND_WIGHT)&&!(i==ISLAND_WIGHT&&j==1)) {
                    cellIdPool.get(currentElement).setNeighbors(new CoordPair(i,j));
                    boardMap[i][j] = cellIdPool.get(currentElement);
                    ++currentElement;
                }
            }
        }
        Integer coastId = NUMBEFOFCELL;
        for(int i = 1; i < ( ISLAND_HIGHT + 1 ); ++i) {
            boardMap[i][0]=new CoastCell(coastId, new CoordPair(i,0));
            ++coastId;
        }

        boardMap[ISLAND_HIGHT][1]=new CoastCell(coastId, new CoordPair(ISLAND_HIGHT,1));
        ++coastId;

        for(int j = 1; j < (ISLAND_WIGHT+1); ++j) {
            boardMap[ISLAND_HIGHT+1][j]=new CoastCell(coastId, new CoordPair(ISLAND_HIGHT+1,j));
            ++coastId;
        }

        boardMap[ISLAND_HIGHT][ISLAND_WIGHT]=new CoastCell(coastId, new CoordPair(ISLAND_HIGHT,ISLAND_WIGHT));
        ++coastId;

        for(int i = ISLAND_HIGHT; i >0; --i) {
            boardMap[i][ISLAND_WIGHT+1]=new CoastCell(coastId, new CoordPair(i,ISLAND_WIGHT+1));
            ++coastId;
        }
        boardMap[1][ISLAND_WIGHT]=new CoastCell(coastId, new CoordPair(1,ISLAND_WIGHT));
        ++coastId;
        for(int j = ISLAND_WIGHT; j > 0; --j) {
            boardMap[0][j]=new CoastCell(coastId, new CoordPair(0,j));
            ++coastId;
        }
        boardMap[1][1]=new CoastCell(coastId, new CoordPair(1,1));
        ++coastId;

        boardMap[0][0]=new MockCell(-1, new CoordPair(0,0));
        boardMap[ISLAND_HIGHT+1][0]=new MockCell(-2, new CoordPair(ISLAND_HIGHT+1,0));
        boardMap[ISLAND_HIGHT+1][ISLAND_WIGHT+1]=new MockCell(-3, new CoordPair(ISLAND_HIGHT+1,ISLAND_WIGHT+1));
        boardMap[0][ISLAND_WIGHT+1]=new MockCell(-4, new CoordPair(0,ISLAND_WIGHT+1));

        //инициализируем игрока. Так-то, это должно быть в конструкторе
        players[0] = new GamePlayer(0);
        players[1] = new GamePlayer(1);

    }

    public CoordPair[] getShipAvailableDirection(Integer playerId){
        return players[playerId].getShipAvailableDirection();
    }

    public CoordPair getShipCord(Integer playerId){
        return players[playerId].getShipCord();
    }

    public boolean moveShip(CoordPair direction, Integer playerId){
        return players[playerId].moveShip(direction);
    }

    public List<MovementResult> movePirat(Movement move, Integer playerId){
        return players[playerId].movePirat(move);
    }

    public AbstractCell getCell(CoordPair cellCord){
        return boardMap[cellCord.getX()][cellCord.getY()];
    }

    public Integer getBoardMapId(Integer x, Integer y) {
        return boardMap[x][y].getId();
    }

    public List<Integer> getBoardMap(){
        List<Integer> tempList = new ArrayList<>();
        for(AbstractCell cellLine[]:boardMap){
            for(AbstractCell cell:cellLine){
                tempList.add(cell.getId());
            }
        }
        return  tempList;
    }

    public CoordPair[] getCellNeighbors(CoordPair cellCord, Integer playerId){
        if(boardMap[cellCord.getX()][cellCord.getY()].isUnderShip){
            return players[playerId].ship.getNeighbors();
        } else {
            return boardMap[cellCord.getX()][cellCord.getY()].getNeighbors();
        }
    }

    public Integer isPirat(CoordPair cord) { //эта штука говорит, есть ли пират в выбранной клетке
        for(GamePlayer player : players) {
            if(player!=null)
                if(player.isPirat(cord)!=null)
                    return player.isPirat(cord) + 3 * player.getPlayerId();
        }
        return -1;
    }

    public CoordPair[] getCellNeighborsByPirat(Integer piratId, Integer playerId){
        return players[playerId].getCellNeighborsByPirat(piratId);
    }

    public Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell, Integer playerId){
        return players[playerId].isCellPlacedNearPirat(piratId, targetCell);
    }

    public CoordPair getPiratCord(Integer piratId, Integer playerId){
        return players[playerId].getPiratCord(piratId);
    }


    private final class GamePlayer{
        private Pirat[] pirats = new Pirat[3];
        private Ship ship;
        private Integer playerId;

        private GamePlayer(Integer playerId){
            this.playerId = playerId;
            for(Integer i = 0; i < 3; ++i){
                generatePirat(i + 3 * playerId, playerId);
            }
            if(playerId.equals(0)){
                setShip(0,new CoordPair(0,6), new CoordPair(0,1));
            } else {
                setShip(1,new CoordPair(ISLAND_HIGHT+1,6), new CoordPair(0,1));
            }
        }

        private CoordPair[] getShipAvailableDirection(){
            return ship.getAvaliableDirection();
        }

        private Integer getPlayerId() {
            return playerId;
        }

        private CoordPair getShipCord(){
            return ship.getLocation();
        }

        private void setShip(Integer id, CoordPair location, CoordPair orientation){
            this.ship = new Ship(id,location, orientation);
            boardMap[location.getX()][location.getY()].setUnderShip(true);
        }

        private void generatePirat(Integer piratId, Integer playerId){
            if(playerId.equals(0)){
                pirats[piratId - 3 * playerId] = new Pirat(piratId, new CoordPair(0, 6)); //переделать
                boardMap[0][6].setPiratId(piratId);
            } else {
                pirats[piratId - 3 * playerId] = new Pirat(piratId, new CoordPair(ISLAND_HIGHT+1, 6));
                boardMap[ISLAND_HIGHT+1][6].setPiratId(piratId);
            }
        }

        private CoordPair getPiratCord(Integer piratId){
            return pirats[piratId -3 * playerId].getLocation(); //сделать поправку на то, что айдишники должны быть уникальны
        }

        private Boolean moveShip(CoordPair direction){
            for(CoordPair tempPair:ship.getAvaliableDirection()){
                if(CoordPair.equals(tempPair,direction)){ // такое направление вообще возможно
                    if(boardMap[ship.getLocation().getX()][ship.getLocation().getY()].getPiratIds().length>0) { //на корабле есть хоть кто-то
                        if (boardMap[CoordPair.sum(ship.neighbors[0], direction).getX()]
                                [CoordPair.sum(ship.neighbors[0], direction).getY()].getId() < NUMBEFOFCELL) { //и с этого корабля потом можно будет сойти на остров
                            for (Integer piratId : boardMap[ship.getLocation().getX()][ship.getLocation().getY()].getPiratIds()) { //айди всех пиратов на корабле
                                pirats[piratId - 3 * playerId].setLocation(CoordPair.sum(ship.getLocation(), direction));
                                boardMap[CoordPair.sum(ship.getLocation(), direction).getX()]
                                        [CoordPair.sum(ship.getLocation(), direction).getY()].setPiratId(piratId - 3 * playerId);
                            }
                            boardMap[ship.getLocation().getX()][ship.getLocation().getY()].setUnderShip(false);
                            ship.setLocation(direction);
                            boardMap[ship.getLocation().getX()][ship.getLocation().getY()].setUnderShip(true);
                            ship.resetNeighbors();
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private List<MovementResult> movePirat(Movement piratMove){
            List<MovementResult> movementResult = new ArrayList<>();
            if(isCellPlacedNearPirat(piratMove.getPiratId(),piratMove.getTargetCell())){ //начальная и конечная клетки заданны корректно
                final Integer starterX = piratMove.getStartCell().getX();
                final Integer starterY = piratMove.getStartCell().getY();
                final Integer targetX = piratMove.getTargetCell().getX();
                final Integer targetY = piratMove.getTargetCell().getY();
                //и тут, по идее, появится еще миллион правил и проверок?
                //миллион - это сколько? Форт с пиратом, да неизвестные клетки с монетой, а что еще?
                if(boardMap[starterX][starterY].piratLeave(piratMove.getPiratId())){
                    //пират успешно покинул клетку
                    pirats[piratMove.getPiratId() - 3 * playerId].setLocation(piratMove.getTargetCell()); //тут тоже что-то может пойти не так

                    movementResult.add(new MovementResult(playerId,piratMove.getPiratId() - 3 * playerId,piratMove.getTargetCell()));
                    //сам пират точно передвинулся, а вот передвинулся ли кто-то еще?
                    //например, в клетке может оказаться крокодил
                    Integer[] deadPirats = boardMap[targetX][targetY].killEnemy(piratMove.getPiratId());
                    //пират, входя в клетку, убивает всех врагов в ней
                    //теперь их надо отправить на родной корабль
                    for(Integer piratId: deadPirats) {
                        System.out.println("Двеннадцать человек на сундук мертвеца!");
                        Integer playerId = piratId / 3;
                        CoordPair shipCord = players[playerId].getShipCord();
                        boardMap[shipCord.getX()][shipCord.getY()].setPiratId(piratId);
                        movementResult.add(new MovementResult(playerId,piratId-3*playerId,shipCord));
                    } //но эту штуку надо будет видеть еще и снаружи, то есть, скорее всего, мы будет возвращать
                    //массив пиратов, у которых сменилась координата
                    boardMap[targetX][targetY].setPiratId(piratMove.getPiratId());
                    return movementResult;
                }
                movementResult.add(new MovementResult(-1));
                return movementResult; //пирата не было в клетке или он не мог ее покинуть
            }
            movementResult.add(new MovementResult(-2));
            return movementResult; // клетки не являлись соседями
        }

        private Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell){
            if(!getCell(getPiratCord(piratId)).getUnderShip()) {
                return getCell(getPiratCord(piratId)).isNeighbors(targetCell);
            } else {
                return ship.isNeighbors(targetCell);
            }
        }

        private CoordPair[] getCellNeighborsByPirat(Integer piratId){
            if(!getCell(players[0].getPiratCord(piratId)).getUnderShip()) {
                return getCell(players[0].getPiratCord(piratId)).getNeighbors();
            } else {
                return ship.getNeighbors();
            }
        }

        private Integer isPirat(CoordPair cord){
            for (Pirat pirat : pirats)
                if (pirat.getLocation().getX().equals(cord.getX()) && pirat.getLocation().getY().equals(cord.getY())) {
                    return pirat.getId();
                }
            return null;
        }
    }
}
