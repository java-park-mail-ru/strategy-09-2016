package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.game.CoordPair;
import ru.mail.park.game.GameBoard;
import ru.mail.park.game.Movement;

@Service
public class GameService {

    private GameBoard board;
    private Movement move;
    private Integer countOfTurns;

    public GameService(){
        board = new GameBoard();
        move = new Movement();
        countOfTurns = 0;
        System.out.println("WTF??");
    }

    public CoordPair getShipCord(Integer playerId){
        return board.getShipCord(playerId);
    }

    public Boolean moveShip(CoordPair direction, Integer playerId){
        ++countOfTurns;
        return board.moveShip(direction, playerId);
    }

    public Boolean movePirat(Integer piratId, CoordPair targetCell, Integer playerId){
        move = new Movement(piratId, getPiratCord(piratId, playerId), targetCell);
        Integer result = board.movePirat(move, playerId); //отдавать один индекс вместо двух
        if(result>-1){
            move = null;
            ++countOfTurns;
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
                            tmpStr = " (0)";
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
