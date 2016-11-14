package ru.mail.park.mechanics.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by victor on 14.11.16.
 */
public class ShipMoveRequest { //то, что придет к нам с клиента
    //запрос на передвижение корабля
    private Integer directionX;
    private Integer directionY;

    @JsonCreator
    public ShipMoveRequest(@JsonProperty("directionX") Integer directionX,
                            @JsonProperty("directionY") Integer directionY){
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public Integer getDirectionX() {
        return directionX;
    }

    public Integer getDirectionY() {
        return directionY;
    }
}
