package ru.mail.park.mechanics.utils;

import ru.mail.park.mechanics.game.CoordPair;

/**
 * Created by victor on 04.12.16.
 */
public class MovementResult {
    private Integer status;

    private Integer playerIngameId;

    private Integer piratId;

    private Integer targetCellIndex;

    public MovementResult(Integer playerId, Integer piratId, CoordPair targetCell){
        this.status = 0;
        this.playerIngameId = playerId;
        this.piratId = piratId;
        this.targetCellIndex = 13*targetCell.getY() + targetCell.getX();
    }

    public MovementResult(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
