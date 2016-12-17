package ru.mail.park.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetNeighbors { //сообщение от пользователя, о том, что он хочет получить окресности клетки
    private Integer cellIndex; // с таким-то индексом
    @JsonCreator
    public GetNeighbors(@JsonProperty("cellIndex") Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }
}
