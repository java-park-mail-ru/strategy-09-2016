package ru.mail.park.mechanics;

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

    public CoordPair getShipCord(Integer playerId){
        return board.getShipCord(playerId);
    }

    public Boolean moveShip(CoordPair direction, Long playerId){
        Integer playerGameId = gameUserIdToGameUserId(playerId);
        ++countOfTurns;
        changeActivePlayer();
        return board.moveShip(direction, playerGameId);
    }

    public Boolean movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        //и сдесь же мы должны тормозить игрока, если сейчас не его ход
        if(activePlayerId!=playerId){
            System.out.println("Какой-то подозрительный юзер. Пытается ходить не в свой ход");
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
            return true;
        } else {
            changeActivePlayer();
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
        StringBuilder builder = new StringBuilder();
        builder.append("<pre>");
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                builder.append("<span id=\"");
                builder.append(13*i+j);
                builder.append("\">");
                String tmpStr = "";
                if(board.getCell(new CoordPair(i,j)).getUnderShip()) {
                    tmpStr = " S ";
                } else {
                    if (board.isPirat(new CoordPair(i, j)) < 0) {
                        tmpStr = Integer.toString(board.getBoardMapId(i, j));
                    } else {
                        if( board.isPirat(new CoordPair(i, j)) > 2 ) {
                            tmpStr = " (2)";
                        } else {
                            tmpStr = " (1)";
                        }
                    }
                }
                //builder.append(tmpStr);
                //tmpStr = board.getCell(new CoordPair(i,j)).getView();
                while (tmpStr.length() < 4) {
                    tmpStr = " " + tmpStr;
                }
                builder.append(tmpStr);
                builder.append(" ");
                builder.append("</span>");
            }
            builder.append("<br>");
        }
        builder.append("</pre>");
        return builder.toString();
    }

    public Integer getCountOfTurns() {
        return countOfTurns;
    }
}
