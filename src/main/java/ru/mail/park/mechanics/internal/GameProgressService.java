package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameContent;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.game.GameBoard;
import ru.mail.park.mechanics.requests.NeighborsMessage;
import ru.mail.park.mechanics.requests.PiratMoveMessage;
import ru.mail.park.mechanics.utils.results.Result;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.MessageToClient;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 14.11.16.
 */
@Service
public class GameProgressService {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameProgressService.class);

    @NotNull
    private final Map<Long, GameContent> usersToGamesMap = new HashMap<>(); //связь юзеров и игр

    @NotNull
    private final RemotePointService remotePointService; // сервис, рассылающие сообщения юзерам

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameProgressService(@NotNull RemotePointService remotePointService){
        this.remotePointService = remotePointService;
    }

    public void createNewGame(@NotNull Long firstPlayerId, @NotNull Long secondPlayerId){
        final GameContent game = new GameContent(firstPlayerId, secondPlayerId);
        usersToGamesMap.put(firstPlayerId, game);
        usersToGamesMap.put(secondPlayerId, game); // создали игру, запомнили ее связь с пользователями
    }

    @Nullable
    public List<Integer> getBoardMap(Long playerId){
        if(usersToGamesMap.containsKey(playerId)) {
            return usersToGamesMap.get(playerId).getMap();
        } else {
            return null;
        }
    }

    public void movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        final MessageToClient.Request infoMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            final List<Result> results = usersToGamesMap.get(playerId).movePirat(piratId, targetCell, playerId);
            if(results==null){
                infoMessage.setMyMessage("Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки");
            } else {
                System.out.println("Пытаемся разослать игрокам результат хода");
                sendUserNewBoard(results, playerId);
                return;
            }
        } else {
            infoMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(infoMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            LOGGER.error("Can't send message to user",e);
        }
    }

    public void sendNeighbord(Integer cellIndex, Long playerId){

        final Integer x = cellIndex % GameBoard.BOARDWIGHT;
        final Integer y = cellIndex / GameBoard.BOARDWIGHT;
        LOGGER.debug("Мы получаем соседей клетки с координатами" + x + ' ' + y);
        final CoordPair piratCord = new CoordPair(x,y);
        final CoordPair[] neighbors = usersToGamesMap.get(playerId).getNeighbors(piratCord, playerId);
        final List<Integer> neighborsList = new ArrayList<>();
        for(CoordPair cell:neighbors){
            neighborsList.add(GameBoard.BOARDWIGHT*cell.getY()+cell.getX());
        }

        if(CoordPair.equals(piratCord,usersToGamesMap.get(playerId).getShipCord(playerId))){
            final CoordPair[] shipNeighbors = usersToGamesMap.get(playerId).getShipAvailableDirection(playerId);
            for(CoordPair cell:shipNeighbors){
                neighborsList.add(GameBoard.BOARDWIGHT*(cell.getY()+piratCord.getY())+(cell.getX()+piratCord.getX()));
            }
        }

        final NeighborsMessage.Request messageWithNeighbors = new NeighborsMessage.Request();
        messageWithNeighbors.setNeighbors(neighborsList);
        try{
            final Message responseMessage = new Message(NeighborsMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(messageWithNeighbors));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            LOGGER.error("Can't send message to user",e);
        }
    }

    public void moveShip(CoordPair direction, Long playerId){
        final MessageToClient.Request infoMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            if(usersToGamesMap.get(playerId).moveShip(direction, playerId)){
                infoMessage.setMyMessage("корабль передвинулся, но мы этого пока не увидим");
            } else {
                infoMessage.setMyMessage("Капитан, корабль не может туда плыть. Сейчас его координаты: ");
            }
        } else {
            infoMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(infoMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            LOGGER.error("Can't send message to user",e);
        }
    }

    private void sendUserNewBoard(List<Result> movementResults, Long playerId){
        final PiratMoveMessage.Request newTurnMessage = new PiratMoveMessage.Request();
        newTurnMessage.setActive(false);
        newTurnMessage.setMovement(new Gson().toJson(movementResults));
        //newTurnMessage.setMovement(movementResults);
        try {
            System.out.println("Пытаемся послать сообщение первому игроку");
            final Message responseMessageToActivePLayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(playerId,responseMessageToActivePLayer);
        } catch( IOException e){
            LOGGER.error("Can't send message to user",e);
        }
        try {
            System.out.println("Пытаемся послать сообщение второму игроку");
            newTurnMessage.setActive(true);
            final Message responseMessageToPassivePlayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(
                    usersToGamesMap.get(playerId).getEnemy(playerId),
                    responseMessageToPassivePlayer);
        } catch( IOException e){
            LOGGER.error("Can't send message to user",e);
        }
    }


}
