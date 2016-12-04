package ru.mail.park.mechanics;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.game.GameBoard;
import ru.mail.park.mechanics.game.Movement;

import java.util.List;

public class GameContent { //класс, управляющий одной отдельно взятой игрой
    private Long firstPlayerId;
    private Long secondPlayerId;
    private GameBoard board;
    private Movement move;
    private Integer countOfTurns;
    private Long activePlayerId;

    public GameContent(Long firstPlayerId, Long secondPlayerId){
        this.firstPlayerId = firstPlayerId;
        this.activePlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.board = new GameBoard();
        this.move = new Movement();
        this.countOfTurns = 0;
    }

    public Integer gameUserIdToGameUserId(@NotNull Long userId){
        if(firstPlayerId.equals(userId)){ //пересчитывает айдишник пользователя в его номер за игровой доской
            return 0;
        } else {
            return 1;
        }
    }

    private void changeActivePlayer(){
        if(activePlayerId.equals(firstPlayerId)){
            activePlayerId = secondPlayerId;
        } else {
            activePlayerId = firstPlayerId;
        }
    }

    public CoordPair[] getNeighbors(CoordPair cellCord, Long playerId){
        final Integer playerIdInGame = gameUserIdToGameUserId(playerId);
        return board.getCellNeighbors(cellCord, playerIdInGame);
    }

    public CoordPair[] getShipAvailableDirection(Long playerId){
        final Integer playerIdInGame = gameUserIdToGameUserId(playerId);
        return board.getShipAvailableDirection(playerIdInGame);
    }

    public CoordPair getShipCord(Long playerId){
        final Integer playerInGameId = gameUserIdToGameUserId(playerId);
        return board.getShipCord(playerInGameId);
    }

    public Boolean moveShip(CoordPair direction, Long playerId){
        final Integer playerGameId = gameUserIdToGameUserId(playerId);
        final Boolean shipMove = board.moveShip(direction, playerGameId);
        if(shipMove) {
            ++countOfTurns;
            changeActivePlayer();
        }
        return shipMove;
    }

    public Boolean movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        //и сдесь же мы должны тормозить игрока, если сейчас не его ход
        if(!activePlayerId.equals(playerId)){
            //System.out.println("Какой-то подозрительный юзер. Пытается ходить не в свой ход");
            //System.out.println(playerId + " " + firstPlayerId + " " + secondPlayerId + " " + activePlayerId);
            return false;
        }
        final Integer playerGameId = gameUserIdToGameUserId(playerId);
        final Integer piratIngameId = piratId + 3 * playerGameId;
//        System.out.println("Пытаемся совершить ход");
//        System.out.println("piratId="+ piratId + " targetX="+targetCell.getX()+" targetCellY="+targetCell.getY());
//        System.out.println(getPiratCord(piratIngameId, playerGameId).getX()+"   " + getPiratCord(piratIngameId, playerGameId).getY());
        move = new Movement(piratIngameId, getPiratCord(piratIngameId, playerGameId), targetCell);
  //      System.out.println("ходит пират с айдишником " + (piratIngameId));
        final Integer result = board.movePirat(move, playerGameId); //отдавать один индекс вместо двух
        if(result>-1){
            move = null;
            ++countOfTurns;
            changeActivePlayer();
            return true;
        } else {
            move = null;
            return false;
        }
    }

    public CoordPair[] getCellNeighborsWithPirat(Integer piratId, Integer playerId ){
        return board.getCellNeighborsByPirat(piratId, playerId); //вспомогательный метод, который выдавал окрестности клеток
    }//по индексу пирата

    public Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell, Integer playerId){
        return board.isCellPlacedNearPirat(piratId, targetCell, playerId); //вспомогательный метод
    } //который говорил, является ли клетка смежной с той, в которой стоит пират с заданным индексом

    public CoordPair getPiratCord(Integer piratId, Integer playerId){
        return board.getPiratCord(piratId, playerId);
    }

    public Integer getMoveStatus(){
        return move.getStatus();
    } // задел на будущее, когда появятся стрелки

    public String getMap(){
        List<Integer> tempList = board.getBoardMap();
        String json = new Gson().toJson(tempList);
       // AbstractCell testCell = board.getCell(new CoordPair(2,2));
       // String testJson = new Gson().toJson(testCell);
       // System.out.println(testJson);
        return json;
    }

    public Integer getCountOfTurns() {
        return countOfTurns;
    } //количество сделанных ходов за партию
}
