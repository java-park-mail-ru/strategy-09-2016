package ru.mail.park.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.mechanics.game.GameBoard;

public class PiratMoveRequest {
    private Integer piratId;
    private Integer targetCellX;
    private Integer targetCellY;

    @JsonCreator
    public PiratMoveRequest(@JsonProperty("piratId") Integer piratId,
                            @JsonProperty("targetCellIndex") Integer targetCellIndex){
        this.piratId = piratId;
        this.targetCellX = targetCellIndex % GameBoard.BOARDWIGHT; //потому что к нам пришел индекс клетки с фронта
        this.targetCellY = targetCellIndex / GameBoard.BOARDWIGHT; // а мы работаем в двумерных координатах
    }

    public Integer getPiratId() {
        return piratId;
    }

    public Integer getTargetCellX() {
        return targetCellX;
    }

    public Integer getTargetCellY() {
        return targetCellY;
    }
}
