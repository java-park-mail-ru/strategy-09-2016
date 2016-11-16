package ru.mail.park.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetNeighbors {
    private Integer cellIndex;
    @JsonCreator
    public GetNeighbors(@JsonProperty("cellIndex") Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }
}
