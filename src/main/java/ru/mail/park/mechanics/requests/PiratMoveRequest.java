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
                            @JsonProperty("targetCellX") Integer targetCellIndex){
        this.piratId = piratId;
        this.targetCellX = targetCellIndex%13;
        this.targetCellY = targetCellIndex / 13;
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
