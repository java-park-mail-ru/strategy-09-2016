package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.game.GameBoard;
import ru.mail.park.mechanics.game.Movement;
import ru.mail.park.mechanics.utils.results.Result;

import java.util.List;

public class GameContent { //класс, управляющий одной отдельно взятой игрой
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameContent.class);

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

    @Nullable
    public List<Result> movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        if(!activePlayerId.equals(playerId)){
            LOGGER.debug("Player try to act in not his own round");
            return null;
        }
        final Integer playerGameId = gameUserIdToGameUserId(playerId);
        final Integer piratIngameId = piratId + 3 * playerGameId;
        move = new Movement(piratIngameId, getPiratCord(piratIngameId, playerGameId), targetCell);
        final List<Result> result = board.movePirat(move, playerGameId); //отдавать один индекс вместо двух
        if(result.get(0).getStatus()>-1){
            move = null;
            ++countOfTurns;
            changeActivePlayer();
            return result;
        } else {
            move = null;
            return null;
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

    public List<Integer> getMap(){
        List<Integer> tempList = board.getBoardMap();
        //String json = new Gson().toJson(tempList);
        return tempList;
    }

    public Integer getCountOfTurns() {
        return countOfTurns;
    } //количество сделанных ходов за партию

    @Nullable
    public Long getEnemy(Long playerId) {
        if(playerId.equals(firstPlayerId)){
            return secondPlayerId;
        } else if(playerId.equals(secondPlayerId)){
            return firstPlayerId;
        } else {
            LOGGER.debug("Player who not involved in game try to get enemy id");
            return null;
        }
    }
}
