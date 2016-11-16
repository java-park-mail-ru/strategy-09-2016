package ru.mail.park.mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.mail.park.game.CoordPair;
import ru.mail.park.game.GameBoard;
import ru.mail.park.game.Movement;

/**
 * Created by victor on 14.11.16.
 */
public class GameContent {
    private Long firstPlayerId;
    private Long secondPlayerId;
    private GameBoard board;
    private Movement move;
    private Integer countOfTurns;
    private Long activePlayerId;
    private ObjectMapper mapper = new ObjectMapper();

    public GameContent(Long firstPlayerId, Long secondPlayerId){
        this.firstPlayerId = firstPlayerId;
        this.activePlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.board = new GameBoard();
        this.move = new Movement();
        this.countOfTurns = 0;
    }

    private Integer gameUserIdToGameUserId(@NotNull Long userId){
        if(firstPlayerId.equals(userId)){
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

    public CoordPair[] getNeighbors(CoordPair cellCord){
        return board.getCellNeighbors(cellCord);
    }

    public CoordPair getShipCord(Long playerId){
        Integer playerInGameId = gameUserIdToGameUserId(playerId);
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
        if(activePlayerId!=(playerId)){
            System.out.println("Какой-то подозрительный юзер. Пытается ходить не в свой ход");
            System.out.println(playerId + " " + firstPlayerId + " " + secondPlayerId + " " + activePlayerId);
            return false;
        }
        Integer playerGameId = gameUserIdToGameUserId(playerId);
        System.out.println("Пытаемся совершить ход");
        System.out.println("piratId="+ piratId + " targetX="+targetCell.getX()+" targetCellY="+targetCell.getY());
        System.out.println(getPiratCord(piratId, playerGameId).getX()+"   " + getPiratCord(piratId, playerGameId).getY());
        move = new Movement(piratId, getPiratCord(piratId, playerGameId), targetCell);
        Integer result = board.movePirat(move, playerGameId); //отдавать один индекс вместо двух
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
        return board.getCellNeighborsByPirat(piratId, playerId);
    }

    public Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell, Integer playerId){
        return board.isCellPlacedNearPirat(piratId, targetCell, playerId);
    }

    public CoordPair getPiratCord(Integer piratId, Integer playerId){
        return board.getPiratCord(piratId, playerId);
    }

    public Integer getMoveStatus(){
        return move.getStatus();
    }

    public String getMap(){
        Integer[] map = new Integer[169];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                map[13*i+j] = board.getBoardMapId(i, j);
                builder.append(Integer.toString(board.getBoardMapId(i, j)));
                builder.append(",");
            }
        }
        /*
        builder.setLength(builder.length()-1);
        try {
            return mapper.writeValueAsString(Arrays.toString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return " ? "; */
        return builder.toString();
    }

    public Integer getCountOfTurns() {
        return countOfTurns;
    }
}
