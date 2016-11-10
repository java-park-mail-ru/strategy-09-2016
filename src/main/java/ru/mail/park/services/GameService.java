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

    public CoordPair getShipCord(){
        return board.getShipCord();
    }

    public Boolean moveShip(CoordPair direction){
        ++countOfTurns;
        return board.moveShip(direction);
    }

    public Boolean movePirat(Integer piratId, CoordPair targetCell){
        move = new Movement(piratId,getPiratCord(piratId),targetCell);
        Integer result = board.movePirat(move); //отдавать один индекс вместо двух
        if(result>-1){
            move = null;
            ++countOfTurns;
            return true;
        } else {
            move = null;
            return false;
        }
    }

    public CoordPair[] getCellNeighborsWithPirat(Integer piratId ){
        return board.getCellNeighborsByPirat(piratId);
    }

    public Boolean isCellPlacedNearPirat(Integer piratId, CoordPair targetCell){
        return board.isCellPlacedNearPirat(piratId, targetCell);
    }

    public CoordPair getPiratCord(Integer piratId){
       return board.getPiratCord(piratId);
    }

    public Integer getMoveStatus(){
        return move.getStatus();
    }

    public String getMap(){
        StringBuilder builder = new StringBuilder();
        builder.append("<pre>");
        for (int i = 0; i < 13; ++i) {
            for (int j = 0; j < 13; ++j) {
                String tmpStr = "";
                if(board.getCell(new CoordPair(i,j)).getUnderShip()) {
                    tmpStr = " S ";
                } else {
                    if (board.isPirat(new CoordPair(i, j)) < 0) {
                        tmpStr = Integer.toString(board.getBoardMapId(i, j));
                    } else {
                        tmpStr = " (*)";
                    }
                }
                //builder.append(tmpStr);
                tmpStr = board.getCell(new CoordPair(i,j)).getView();
                while (tmpStr.length() < 4) {
                    tmpStr = " " + tmpStr;
                }
                builder.append(tmpStr);
                builder.append(" ");
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
