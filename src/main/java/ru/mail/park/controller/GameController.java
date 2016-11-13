package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.game.CoordPair;
import ru.mail.park.services.GameService;

@RestController
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(path = "/test/map", method = RequestMethod.GET)
    public ResponseEntity map() {
        return ResponseEntity.ok(gameService.getMap());
    }

    @RequestMapping(path = "/test/move", method = RequestMethod.POST)
    public ResponseEntity move(@RequestBody MoveRequest moveRequest) {
        StringBuilder builder = new StringBuilder();
        Integer playerId = moveRequest.getPlayerId();
        if(moveRequest.getStatus()==null||moveRequest.getStatus()==0){
            builder.append("Выберите пирата:");
            for(int i = 0; i < 3; ++i) {
                builder.append("\n");
                builder.append("Пират ");
                builder.append(i);
                builder.append(" в клетке с координатами X: ");
                builder.append(gameService.getPiratCord(i,playerId).getX());
                builder.append(" и  Y:");
                builder.append(gameService.getPiratCord(i, playerId).getY());
            }
            builder.append("\n");
            builder.append("Корабль находится");
            builder.append(" в клетке с координатами X: ");
            builder.append(gameService.getShipCord(playerId).getX());
            builder.append(" и  Y:");
            builder.append(gameService.getShipCord(playerId).getY());
        }
        if(moveRequest.getStatus()==1){
            moveRequest.getShipChoosen();
            Integer piratId = moveRequest.getPiratId();
            builder.append("Вы выбрали пирата ");
            builder.append(piratId);
            builder.append(", теперь выберите клетку для хода:");
            for(int i = 0; i < gameService.getCellNeighborsWithPirat(piratId, playerId).length; ++i){
                if(gameService.getCellNeighborsWithPirat(piratId, playerId)[i].getX()!=-1) {
                    builder.append("\n");
                    builder.append(i);
                    builder.append(": Клетка с координатами X: ");
                    builder.append(gameService.getCellNeighborsWithPirat(piratId, playerId)[i].getX());
                    builder.append(" и  Y:");
                    builder.append(gameService.getCellNeighborsWithPirat(piratId, playerId)[i].getY());
                }
            }
        }
        if(moveRequest.getStatus()==2){
            Integer piratId = moveRequest.getPiratId();
            CoordPair targetCell = moveRequest.getTargetCell();
            if(!gameService.isCellPlacedNearPirat(piratId,targetCell, playerId)){
                return ResponseEntity.ok("Выбрана неправильна клетка");
            }
            if(gameService.movePirat(piratId,targetCell, playerId)) {
                builder.append("Пират ");
                builder.append(piratId);
                builder.append(" передвинут в клетку с координатами X: ");
                builder.append(targetCell.getX());
                builder.append("и Y: ");
                builder.append(targetCell.getY());
                builder.append("\n");
                builder.append("Всего было сделанно ходов ");
                builder.append(gameService.getCountOfTurns());
            } else {
                builder.append("Движение нарушает правила");
            }
        }
        if(moveRequest.getStatus()==3){//мы решили двигать корабль
            CoordPair choosenDirection= moveRequest.getShipMoveDirection();
            if(gameService.moveShip(choosenDirection, playerId)){
                builder.append("Корабль успешно передвинут");
            } else {
                builder.append("Корабль не смог передвинуться в выбранную клетку");
            }
        }
        return ResponseEntity.ok(builder.toString());
    }


    private static final class MoveRequest {
        private final CoordPair targetCell;
        private final Integer status;
        private final Integer piratId;
        private final Boolean isShipChoosen;
        private final CoordPair shipMoveDirection;
        private final Integer playerId;

        @JsonCreator
        private MoveRequest(@JsonProperty("status") Integer status,
                                      @JsonProperty("x") Integer x,
                                      @JsonProperty("y") Integer y,
                                      @JsonProperty("piratId") Integer piratId,
                            @JsonProperty("isShipChoosen") Boolean isShipChoosen,
                            @JsonProperty("shipMoveX") Integer shipMoveX,
                            @JsonProperty("shipMoveY") Integer shipMoveY,
                            @JsonProperty("playerId") Integer playerId){
            this.playerId = playerId;
            this.isShipChoosen = isShipChoosen;
            this.status = status;
            this.targetCell = new CoordPair(x,y);
            this.piratId = piratId;
            this.shipMoveDirection = new CoordPair(shipMoveX, shipMoveY);
        }

        public Integer getPlayerId() {
            return playerId;
        }

        public CoordPair getShipMoveDirection() {
            return shipMoveDirection;
        }

        public Integer getStatus() {
            return status;
        }

        public CoordPair getTargetCell() {
            return targetCell;
        }

        public Integer getPiratId() {
            return piratId;
        }

        public Boolean getShipChoosen() {
            return isShipChoosen;
        }
    }


}
