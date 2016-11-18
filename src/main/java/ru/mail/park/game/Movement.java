package ru.mail.park.game;

public class Movement {
    private Integer status;
    private Integer piratId;
    private CoordPair startCell;
    private CoordPair targetCell;

    public Movement(){
        status = 1;
    }

    public Movement(Integer piratId, CoordPair startCell, CoordPair targetCell) {
        this.piratId = piratId;
        this.startCell = startCell;
        this.targetCell = targetCell;
    }

    public Integer getPiratId() {
        return piratId;
    }

    public CoordPair getStartCell() {
        return startCell;
    }

    public CoordPair getTargetCell() {
        return targetCell;
    }

    public Integer getStatus() {
        return status;
    }
}
