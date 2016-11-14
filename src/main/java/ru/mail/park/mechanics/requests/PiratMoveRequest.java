package ru.mail.park.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by victor on 14.11.16.
 */
public class PiratMoveRequest {
    private Integer piratId;
    private Integer targetCellX;
    private Integer targetCellY;

    @JsonCreator
    public PiratMoveRequest(@JsonProperty("piratId") Integer piratId,
                            @JsonProperty("targetCellX") Integer targetCellX,
                            @JsonProperty("targetCellY") Integer targetCellY){
        this.piratId = piratId;
        this.targetCellX = targetCellX;
        this.targetCellY = targetCellY;
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
