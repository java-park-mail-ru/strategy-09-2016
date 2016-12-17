package ru.mail.park.mechanics.utils.results;

import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.game.GameBoard;

/**
 * Created by victor on 04.12.16.
 */
public class MovementResult extends  Result {

    private final ResultType type = Result.ResultType.PiratMove;

    private Integer piratId;

    private Integer targetCellIndex;

    public MovementResult(Integer playerId, Integer piratId, CoordPair targetCell){
        super(0,playerId);
        this.piratId = piratId;
        this.targetCellIndex = GameBoard.BOARDWIGHT * targetCell.getY() + targetCell.getX();
    }

    public MovementResult(Integer status){
        super(status);
    }

    public Integer getStatus() {
        return super.status;
    }

    public void setStatus(Integer status) {
        super.status = status;
    }
}
